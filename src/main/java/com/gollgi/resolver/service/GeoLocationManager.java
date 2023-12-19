package com.gollgi.resolver.service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * @author Hoffman
 *
 */
@Service
public class GeoLocationManager {

	/**
	 * Spring dependency injection
	 */
	@Autowired
	private GeoProviderHttpClient geoHttpClient;
	
	@Autowired
	private GeoLocationUtils geoUtils;

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
		if (!geoUtils.isResolverSupported(resolverName)) {
			return Response.status(200).entity(new WebErrorResponse(GeoResolverConstants.API_INVOCATION_FAILURE, GeoResolverConstants.UNSUPPORTED_RESOLVER)).build();
		}

		// Create a {@link GeoLocation} and (@link GeoApiResolver} objects
		GeoLocation geoLocation = new GeoLocation();
		GeoApiResolver resolver = GeoApiResolver.valueOf(resolverName.toUpperCase());

		// Call the desired gio-location provider and retrieve a new (@link GeoLocation}  object
		switch (resolver) {
		case OPEN_STREET_MAP:
			geoLocation = geoHttpClient.getOpenStreetMapGeoLocation(address);
			break;
		case BING:
			geoLocation = geoHttpClient.getBingQMapGeoLocation(address);
			break;
		case HERE:
			geoLocation = geoHttpClient.getHereMapGeoLocation(address);
			break;
		default:
			throw new IllegalArgumentException(String.format("No Implementation for the chosen resolver %s", resolver)) ;
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
		//In order to produce the best result we will invoke each resolver and calculate the results 
		List<GeoLocation>  locationsAttrsScoreCache = new ArrayList<GeoLocation>();
		
		// Remove any char that is not a string or integer from the given address
		address = address.replaceAll("[^a-zA-Z0-9]+", " ");
		GeoLocation geoLocation = new GeoLocation();

		// try to resolve {@link GeoLocation} via OpenStreetMap
		geoLocation = geoHttpClient.getOpenStreetMapGeoLocation(address);
		locationsAttrsScoreCache.add(geoLocation);

		// try to resolve {@link GeoLocation} via BingQ
		geoLocation = geoHttpClient.getBingQMapGeoLocation(address);
		locationsAttrsScoreCache.add(geoLocation);

		// try to resolve {@link GeoLocation} via Here
		geoLocation = geoHttpClient.getHereMapGeoLocation(address);
		locationsAttrsScoreCache.add( geoLocation);

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
			}
		}

		//Resolve the Best value {@link GeoLocation} from the locationsAttrsScoreCache Map and return the HTTP response
		GeoLocation finalLocation = calculateBestResolverResults(locationsAttrsScoreCache, address) ; 
		return Response.status(200).entity(finalLocation).build();
	}

	/**
	 * The Method Will get the {@link GeoLocation} object with the best result and that have the least null attributes 
	 * From the locationsAttrsScoreCache Map
	 * @param locationsCache - {@link Map} containing all geo-location resolver results for calculation
	 * @param address - the requested address from which we retrieved data from the resolvers
	 * @return {@link GeoLocation} - the calculated object contains the least null attributes 
	 */
	private GeoLocation calculateBestResolverResults(List<GeoLocation> locationsCache, String address) {
		//Get the richest result from BingQ resources array
		Optional<GeoLocation> richestObject = 
				locationsCache
				.stream()
				.filter(location -> location!=null)
				.max((location1, location2) -> {
					int firstLocationAttrNumber = this.calculateExistingAttributeNum(location1);
					int secondLocationAttrNumber = this.calculateExistingAttributeNum(location2);
					return Integer.compare(secondLocationAttrNumber, firstLocationAttrNumber);
				});
		
		GeoLocation  finalLocation = richestObject.orElseThrow(()-> new RuntimeException("GeoLocation cannot be null"));
		locationsCache.remove(finalLocation) ;
		
		Arrays.stream(finalLocation.getClass().getDeclaredFields()).forEach(field -> {
			field.setAccessible(true); 
			try {
				Object attribute = field.get(finalLocation); 
				if (attribute == null ||  StringUtils.isEmpty(attribute.toString())) {
					String resolvedAttribute = this.getAttributeFromLocationsCache(locationsCache,field.getName(),finalLocation) ;
					field.set(finalLocation, resolvedAttribute);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}); 
		
		 return finalLocation ;
	}
	
	/**
	 * In case the  final {@link GeoLocation} still contains a null or empty attribute - try to resolve this 
	 * attribute from other {@link GeoLocation} object
	 * since this attribute might be present 
	 * @param locationsAttrsScoreCache - all resolved {@link GeoLocation} 
	 * @param finalLocation 
	 * @return {@link String} new value for the desired getter/setter
	 */
private String getAttributeFromLocationsCache(List<GeoLocation> locationsCache, String attribueToSearch, 
		GeoLocation finalLocation) {
	return (String) locationsCache.stream().map(location -> {
		Object field;
		try {
			field = new PropertyDescriptor(attribueToSearch, GeoLocation.class).getReadMethod().invoke(location);
			if (field != null && !StringUtils.isEmpty(field.toString())) {
				finalLocation.getResolvers().addAll(location.getResolvers());
				return field.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}).findFirst().orElse(null);
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
