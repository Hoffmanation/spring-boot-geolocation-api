package com.gollgi.resolver.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger ;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gollgi.resolver.app.objects.GeoApiResolver;
import com.gollgi.resolver.entity.GeoLocation;
import com.gollgi.resolver.util.GeoLocationUtils;
import com.gollgi.resolver.util.GeoResolverConstants;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * An Http Client class that provide method to consume geo location providers
 * API's
 * 
 * @author Hoffman
 *
 */
@Service
public class GeoProviderHttpClient {
	private static final Logger logger = Logger.getLogger(GeoProviderHttpClient.class);

	@Autowired
	private GeoLocationUtils geoUtils;

	/**
	 * Http client method for consuming 'HERE' geo-location REST services
	 * 
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public GeoLocation getHereMapGeoLocation(String searchWord) {
		List<GeoApiResolver> resolvers = new ArrayList<>();
		resolvers.add(GeoApiResolver.HERE);
		
		try {
			//Call Here REST endpoint and retrieve Geo-Location Data
			searchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
			Builder webResource = geoUtils.getHttpClient(GeoResolverConstants.HERE_API + searchWord);
			ClientResponse response = webResource.get(ClientResponse.class);
			String output = response.getEntity(String.class);

			//Ensure valid response
			if (!geoUtils.isResponseValid(response,output)) {
				logger.debug(String.format("GeoApiResolver.HERE invocation failed ClientResponse: %s", response.toString()));
				return geoUtils.getDefaultGeoLocation(resolvers);
			}

			//Parse response and Get parents JSON objects
			JSONObject hereResponseObject = geoUtils.getJsonObject(output);
			JSONObject responseObject = (JSONObject) hereResponseObject.get("Response");
			JSONArray viewObject = (JSONArray) responseObject.get("View");
			JSONObject firstViewInnerObject = (JSONObject) viewObject.get(0);
			JSONArray resultsObject = (JSONArray) firstViewInnerObject.get("Result");
			JSONObject firstResultInnerObject = (JSONObject) resultsObject.get(0);
			JSONObject locationObject = (JSONObject) firstResultInnerObject.get("Location");
			JSONObject displayPositionObject = (JSONObject) locationObject.get("DisplayPosition");
			JSONObject addressObject = (JSONObject) locationObject.get("Address");
			
			//Retrieving address and misc Data
			String countryName = (String) addressObject.get("Country");
			String State = (String) addressObject.get("State");
			String region = (String) addressObject.get("County");
			String city = (String) addressObject.get("City");
			String displayName = (String) addressObject.get("Locality");
			String postalCode = (String) addressObject.get("PostalCode");
			String address = (String) addressObject.get("address");

			//Retrieving coordinates Data
			String latitude = (String) displayPositionObject.get("Latitude");
			String longitude = (String)  displayPositionObject.get("Longitude");
			
			//Time zone and resolver type
			TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
			int timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);
			
			return new GeoLocation( timeZoneOffset,  null,  countryName,  region,  city,
					 postalCode,  latitude,  longitude,  displayName, resolvers,
					 address,  null) ;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return geoUtils.getDefaultGeoLocation(resolvers);
	}

	/**
	 * Http client method for consuming 'BING-Q' geo-location REST services
	 * 
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	@SuppressWarnings("unchecked")
	public GeoLocation getBingQMapGeoLocation(String searchWord) {
		List<GeoApiResolver> resolvers = new ArrayList<>();
		resolvers.add(GeoApiResolver.BING);
		
		try {
			//Call BingQ REST endpoint and retrieve Geo-Location Data
			searchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
			Builder webResource = geoUtils.getHttpClient(GeoResolverConstants.BING_API.replace("{}",searchWord));
			ClientResponse response = webResource.get(ClientResponse.class);
			String output = response.getEntity(String.class);
			
			//Ensure valid response
			if (!geoUtils.isResponseValid(response,output)) {
				logger.debug(String.format("GeoApiResolver.BING invocation failed ClientResponse: %s", response.toString()));
				return geoUtils.getDefaultGeoLocation(resolvers);
			}
			
			//Parse response and Get parents JSON objects
			logger.info(String.format("GeoApiResolver.BING ClientResponse: %s", output));
			JSONObject bingResponseObject = geoUtils.getJsonObject(output);
			JSONArray resourceSetsArray = (JSONArray) bingResponseObject.get("resourceSets");
			JSONObject resourceSetsFirst = (JSONObject) resourceSetsArray.get(0);
			JSONArray resourcesArray = (JSONArray) resourceSetsFirst.get("resources");
			
			//Get the richest result from BingQ resources array
			Optional<JSONObject> richestObject = 
					resourcesArray
					.stream()
					.max((resource1, resource2) -> {
						JSONObject firstObject = (JSONObject) resource1;
						JSONObject secondObject = (JSONObject) resource2;
						JSONObject firstAddress = (JSONObject) firstObject.get("address");
						JSONObject secondAddress = (JSONObject) secondObject.get("address");
						return Integer.compare(firstAddress.size(), secondAddress.size());
					});
			
			JSONObject resourcesRichestObject =  (JSONObject) richestObject.get() ;
			JSONObject addressObject = (JSONObject) resourcesRichestObject.get("address");
			JSONObject pointObject = (JSONObject) resourcesRichestObject.get("point");
			JSONArray coordinates = (JSONArray) pointObject.get("coordinates");
			
			//Retrieving address Data
			String region = (String) addressObject.get("adminDistrict2");
			if (StringUtils.isEmpty(region)) region = (String) addressObject.get("locality");
			String countryName = (String) addressObject.get("countryRegion");
			String postalCode = (String) addressObject.get("postalCode");
			String city = (String) addressObject.get("adminDistrict");
			String formattedAddress = (String) addressObject.get("formattedAddress");
			
			//Retrieving coordinates Data

			String latitude = String.valueOf(coordinates.get(0));
			String longitude = String.valueOf(coordinates.get(1));

			//Time zone and resolver type
			TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
			int timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);
			
			return new GeoLocation( timeZoneOffset,  null,  countryName, region,  city,
					 postalCode,  latitude,  longitude,  formattedAddress,  resolvers,
					 formattedAddress,  null);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return geoUtils.getDefaultGeoLocation(resolvers);
	}

	/**
	 * Http client method for consuming 'OPEN STREET MAP' geo-location REST services
	 * 
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public GeoLocation getOpenStreetMapGeoLocation(String searchWord) {
		List<GeoApiResolver> resolvers = new ArrayList<>();
		resolvers.add(GeoApiResolver.OPEN_STREET_MAP);

		try {
			//Call OpenStreetMap REST endpoint and retrieve Geo-Location Data
			String formattedSearchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
			Builder jerseyBuilder = geoUtils.getHttpClient(GeoResolverConstants.OPEN_STREET_MAP_API.replace("{}",formattedSearchWord));
			ClientResponse response = jerseyBuilder.get(ClientResponse.class);
			String output = response.getEntity(String.class);
			
			//Ensure valid response
			if (!geoUtils.isResponseValid(response,output)) {
				logger.debug(String.format("GeoApiResolver.OPEN_STREET_MAP invocation failed ClientResponse: %s", response.toString()));
				return geoUtils.getDefaultGeoLocation(resolvers);
			}
			
			//Parse response and Get parents JSON objects
			logger.info(String.format("GeoApiResolver.OPEN_STREET_MAP ClientResponse: %s", output));
			JSONArray openStreetResultObject = geoUtils.getJsonObject(output);
			JSONObject onlyInnerObject = (JSONObject) openStreetResultObject.get(0);
			JSONObject addressObject = (JSONObject) onlyInnerObject.get("address");

			//Retrieving misc Data
			String displayName = (String) onlyInnerObject.get("display_name");

			//Retrieving address Data
			String countryName = (String) addressObject.get("country");
			String countryCode = (String) addressObject.get("country_code");
			String town = (String) addressObject.get("town");
			String road = (String) addressObject.get("road");
			String city = (String) addressObject.get("city");
			String ISO = (String) addressObject.get("ISO3166-2-lvl4");
			String region = (String) addressObject.get("state_district");
			String county = (String) addressObject.get("county");
			String postalCode = (String) addressObject.get("postcode");
			String house_number = (String) addressObject.get("v");
			String state = (String) addressObject.get("state");
			
			//QA adaptation for lower levels
			if (StringUtils.isEmpty(city)) city = StringUtils.substringBefore(searchWord,",") ;
			
			//Retrieving coordinates Data
			String latitude = (String) onlyInnerObject.get("lat");
			String longitude = (String) onlyInnerObject.get("lon");
			
			//Time zone and resolver type
			TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
			int timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);

			return new GeoLocation( timeZoneOffset,  countryCode,  countryName,  region,  city,
					 postalCode,  latitude,  longitude,  displayName, resolvers,
					 null,  ISO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return geoUtils.getDefaultGeoLocation(resolvers);
	}

}
