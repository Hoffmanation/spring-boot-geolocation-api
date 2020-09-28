package com.gollgi.resolver.service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gollgi.resolver.app.objects.GeoApiResolver;
import com.gollgi.resolver.entity.GeoLocation;
import com.gollgi.resolver.model.WebErrorResponse;
import com.gollgi.resolver.util.GeoLocationUtils;
import com.gollgi.resolver.util.GeoResolverConstants;

/**
 * Service manager for the {@link GeoLocation} entity
 * 
 * @author oreng
 *
 */
@Service
public class GeoLocationManager {

	/**
	 * Spring dependency injection
	 */
	@Autowired
	private GeoProviderHttpClient geoHttpClient;

	/**
	 * This method will invoke a specific geo-location provider in order to retrieve a {@link GeoLocation} object
	 * 
	 * @param address      - The desired address from which the resolver will return the geo-location.
	 * @param resolverName - The name of the geo-location provider (resolver)
	 * @return {@link Response} - An HTTP response containing the {@link GeoLocation} object
	 */
	public Response getGeoLocationByResolver(String address, String resolverName) {
		// Validate that this provider is exist in the system and the application can
		// consume he's geo-location services
		if (!GeoLocationUtils.isResolverSupported(resolverName)) {
			return Response.status(200).entity(new WebErrorResponse(GeoResolverConstants.API_INVOCATION_FAILURE, GeoResolverConstants.UNSUPPORTED_RESOLVER)).build();
		}

		// Create a {@link GeoLocation} and (@link GeoApiResolver} objects
		GeoLocation geoLocation = new GeoLocation();
		GeoApiResolver resolver = GeoApiResolver.valueOf(resolverName.toUpperCase());

		// Call the desired gio-location provider and retrieve a new (@link
		// GeoApiResolver} object
		switch (resolver) {
		case OPEN_STREET_MAP:
			geoLocation = geoHttpClient.getOpenStreetMapGeoLocation(address);
			break;
		case BING:
			geoLocation = geoHttpClient.getBingMapGeoLocation(address);
			break;
		case GOOGLE:
			geoLocation = geoHttpClient.getGoggleCompanyGeoLocationEurope(address);
			break;
		case HERE:
			geoLocation = geoHttpClient.getHereMapGeoLocation(address);
			break;
		}

		return Response.status(200).entity(geoLocation).build();
	}

	/**
	 * This method will invoke a random geo-location provider in order to retrieve a
	 * {@link GeoLocation} object
	 * 
	 * @param address - The desired address from which the resolver will return the  {@link GeoLocation}
	 * @return {@link Response} - An HTTP response containing the {@link GeoLocation} object
	 */
	public Response getGeoLocation(String address) {
		//TODO -  IMPROVE JSON PARSING 
		//In order to produce the best result we will invoke each resolver and calculate the results 
		Map<Integer, GeoLocation>  locationsAttrsScoreCache = new HashMap<Integer, GeoLocation>();
		
		// Remove any char that is not a string or integer from the given address
		address = address.replaceAll("[^a-zA-Z0-9]+", " ");
		GeoLocation geoLocation = new GeoLocation();

		// try to resolve {@link GeoLocation} via OpenStreetMap
		geoLocation = geoHttpClient.getOpenStreetMapGeoLocation(address);
		Integer openStreetMapNullAttrs= calculateExistingAttributeNum(geoLocation);
		locationsAttrsScoreCache.put(openStreetMapNullAttrs, geoLocation);

		// try to resolve {@link GeoLocation} via BingQ
		geoLocation = geoHttpClient.getBingQMapGeoLocation(address);
		Integer bingNullAttrs= calculateExistingAttributeNum(geoLocation);
		locationsAttrsScoreCache.put(bingNullAttrs, geoLocation);

		// try to resolve {@link GeoLocation} via Here
		geoLocation = geoHttpClient.getHereMapGeoLocation(address);
		Integer hereNullAttrs= calculateExistingAttributeNum(geoLocation);
		locationsAttrsScoreCache.put(hereNullAttrs, geoLocation);

		// If we did not managed to retrieve a {@link GeoLocation} try to invoke the
		// same provider but with postal code instead of the address
		if (geoLocation.isEmpty()) {
			String postCode = StringUtils.EMPTY;
			try {
				String pattern = " [\\w\\d]{3,4} [\\w\\d]{3}( |$)";
				Pattern r = Pattern.compile(pattern);
				Matcher matcher = r.matcher(address);
				if (matcher.find()) {
					for (int i = 0; i < matcher.groupCount(); i++) {
						postCode = matcher.group(i).trim();
					}
				}
			} catch (Exception e) {
			}

			if (geoLocation.isEmpty() && !postCode.isEmpty() && postCode.length() < 9) {
				geoLocation = geoHttpClient.getOpenStreetMapGeoLocation(postCode);
				if (geoLocation.isEmpty()) {
					geoLocation = geoHttpClient.getBingMapGeoLocation(postCode);
				}
			}
		}

		//Resolve the Best value {@link GeoLocation} from the locationsAttrsScoreCache Map and return the HTTP response
		GeoLocation finalLocation = calculateBestResolverResults(locationsAttrsScoreCache, address) ; 
		return Response.status(200).entity(finalLocation).build();
	}

	/**
	 * The Method Will get the {@link GeoLocation} object with the best result and that have the least null attributes 
	 * From the locationsAttrsScoreCache Map
	 * @param locationsAttrsScoreCache - {@link Map} containing all geo-location resolver results for calculation
	 * @param address - the requested address from which we retrieved data from the resolvers
	 * @return {@link GeoLocation} - the calculated object contains the least null attributes 
	 */
	private GeoLocation calculateBestResolverResults(Map<Integer, GeoLocation> locationsAttrsScoreCache, String address) {
		Optional<Integer> lowestNullNum = locationsAttrsScoreCache.keySet().stream().filter(entry -> entry!=null) .min((i, j) -> i.compareTo(j)); 
		GeoLocation finalLocation =locationsAttrsScoreCache.get(lowestNullNum.get()) ;
		 finalLocation .setAddress(address);
		 return finalLocation ;
	}
	
/**
 * This method will loop through a given {@link GeoLocation} object in order to calculate how many null getter attribute it contains
 * @param geoLocation - the {@link GeoLocation} object  to calculate the number of null attributes 
 * @return {@link Integer} - the number of null attribute for this {@link GeoLocation} object
 */
	private Integer calculateExistingAttributeNum(GeoLocation geoLocation) {
		Integer nullAttributes = 0;
		try {
			PropertyDescriptor[] propDescArr = Introspector.getBeanInfo(GeoLocation.class, Object.class).getPropertyDescriptors();
			nullAttributes = (int) Arrays.stream(propDescArr).filter(isGeoLocationGetterAttrIsNull(geoLocation)).count();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nullAttributes;

	}

	/**
	 * This method will receive a {@link GeoLocation} object and will validate if
	 * the getter attribute is null or not
	 * 
	 * @param location - Object on which we will validate null getter attributes
	 * @return {@link Predicate} contains a boolean value which indicates if attribute getter returned null or null
	 */
	private Predicate<PropertyDescriptor> isGeoLocationGetterAttrIsNull(GeoLocation location) {
		return pd -> {
			Method getterMethod = pd.getReadMethod();
			boolean result = false;
			try {
				result = (getterMethod != null && getterMethod.invoke(location) == null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		};
	}

}
