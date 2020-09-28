package com.gollgi.resolver.service;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.gollgi.resolver.app.objects.GeoApiResolver;
import com.gollgi.resolver.entity.GeoLocation;
import com.gollgi.resolver.util.GeoResolverConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * An Http Client class that provide method to consume geo location providers API's
 * @author oreng
 *
 */
@Service
public class GeoProviderHttpClient {
	private static final Logger logger = Logger.getLogger(GeoProviderHttpClient.class);

	
	/**
	 * Http client method for consuming 'HERE' geo-location REST services
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public  GeoLocation getHereMapGeoLocation(String searchWord) {
		int timeZoneOffset = 0;
		String region = null;
		String countryName = null;
		String countryCode = null;
		String postalCode = null;
		String output = null;
		String latitude = null;
		String longitude = null;
		String city = null;
		JSONParser parser = new JSONParser();
		searchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
		Client client = Client.create();
		WebResource webResource = client.resource(GeoResolverConstants.HERE_ADDRESS_API_PREFIX + searchWord);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			return new GeoLocation();
		}
		output = response.getEntity(String.class);
		Object obj;
		try {
			obj = parser.parse(output);
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject jsonResults = (JSONObject) jsonObject.get("Response");
			JSONArray results = (JSONArray) jsonResults.get("View");
			for (Object object : results) {
				JSONObject objAtIndex = (JSONObject) object;
				JSONArray inner = (JSONArray) objAtIndex.get("Result");
				for (Object object2 : inner) {
					JSONObject objAtIndex2 = (JSONObject) object2;
					JSONObject inner2 = (JSONObject) objAtIndex2.get("Location");

					JSONObject displayPosition = (JSONObject) inner2.get("DisplayPosition");
					latitude = String.valueOf(displayPosition.get("Latitude"));
					longitude = String.valueOf(displayPosition.get("Longitude"));

					JSONObject jsonAddress = (JSONObject) inner2.get("Address");
					countryName = (String) jsonAddress.get("Country");
					countryName = (String) jsonAddress.get("State");
					region = (String) jsonAddress.get("County");
					city = (String) jsonAddress.get("City");
					postalCode = (String) jsonAddress.get("PostalCode");

				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);
		return new GeoLocation(timeZoneOffset, countryCode, countryName, region, city, postalCode, latitude, longitude, GeoApiResolver.HERE);
	}
	
	
	/**
	 * Http client method for consuming 'BING' geo-location REST services
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public  GeoLocation getBingMapGeoLocation(String searchWord) {
		int timeZoneOffset = 0;
		String region = null;
		String countryName = null;
		String countryCode = null;
		String postalCode = null;
		String output = null;
		String latitude = null;
		String longitude = null;
		String city = null;
		JSONParser parser = new JSONParser();
		searchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
		Client client = Client.create();
		WebResource webResource = client.resource(GeoResolverConstants.BING_ADDRESS_API_PREFIX + searchWord + GeoResolverConstants.BING_ADDRESS_API_SUFFIX);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			return new GeoLocation();
		}
		output = response.getEntity(String.class);
		Object obj;
		try {
			obj = parser.parse(output);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray results = (JSONArray) jsonObject.get("resourceSets");
			for (Object object : results) {
				JSONObject objAtIndex = (JSONObject) object;
				JSONArray inner = (JSONArray) objAtIndex.get("resources");
				for (Object object2 : inner) {
					JSONObject objAtIndex2 = (JSONObject) object2;
					JSONObject inner2 = (JSONObject) objAtIndex2.get("address");
					region = (String) inner2.get("adminDistrict2");
					countryName = (String) inner2.get("countryRegion");
					postalCode = (String) inner2.get("postalCode");
					city = (String) inner2.get("locality");
					countryName = (String) inner2.get("adminDistrict");
					JSONObject inner3 = (JSONObject) objAtIndex2.get("point");
					JSONArray coordinates = (JSONArray) inner3.get("coordinates");
					latitude = String.valueOf(coordinates.get(0));
					longitude = String.valueOf(coordinates.get(1));
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);

		return new GeoLocation(timeZoneOffset, countryCode, countryName, region, city, postalCode, latitude, longitude, GeoApiResolver.BING);
	}
	

	/**
	 * Http client method for consuming 'BING-Q' geo-location REST services
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public  GeoLocation getBingQMapGeoLocation(String searchWord) {
		int timeZoneOffset = 0;
		String region = null;
		String countryName = null;
		String countryCode = null;
		String postalCode = null;
		String output = null;
		String latitude = null;
		String longitude = null;
		String city = null;
		JSONParser parser = new JSONParser();
		searchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
		Client client = Client.create();
		WebResource webResource = client.resource(GeoResolverConstants.BING_Q_API_PREFIX + searchWord + GeoResolverConstants.BING_ADDRESS_API_SUFFIX);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			return new GeoLocation();
		}
		output = response.getEntity(String.class);
		Object obj;
		try {
			obj = parser.parse(output);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray results = (JSONArray) jsonObject.get("resourceSets");
			for (Object object : results) {
				JSONObject objAtIndex = (JSONObject) object;
				JSONArray inner = (JSONArray) objAtIndex.get("resources");
				for (Object object2 : inner) {
					JSONObject objAtIndex2 = (JSONObject) object2;
					JSONObject inner2 = (JSONObject) objAtIndex2.get("address");

					region = (String) inner2.get("adminDistrict2");
					countryName = (String) inner2.get("countryRegion");
					postalCode = (String) inner2.get("postalCode");
					city = (String) inner2.get("locality");
					countryName = (String) inner2.get("adminDistrict");

					JSONObject inner3 = (JSONObject) objAtIndex2.get("point");
					JSONArray coordinates = (JSONArray) inner3.get("coordinates");
					latitude = String.valueOf(coordinates.get(0));
					longitude = String.valueOf(coordinates.get(1));

				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);

		return new GeoLocation(timeZoneOffset, countryCode, countryName, region, city, postalCode, latitude, longitude, GeoApiResolver.BING);
	}

	/**
	 * Http client method for consuming 'GOOGLE' geo-location REST services
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public  GeoLocation getGoggleCompanyGeoLocationEurope(String search) {
		int timeZoneOffset = 0;
		String region = null;
		String countryName = null;
		String countryCode = null;
		String postalCode = null;
		String output = null;
		String latitude = null;
		String longitude = null;
		String city = null;
		JSONParser parser = new JSONParser();
		try {
			search = search.replace(" ", "%20");
			Client client = Client.create();
			WebResource webResource = client.resource(GeoResolverConstants.GOOGLE_ADDRESS_API_PREFIX + search + GeoResolverConstants.GOOGLE_API_KEY);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			Object obj = parser.parse(output);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray results = (JSONArray) jsonObject.get("results");
			for (Object object : results) {
				JSONObject objAtIndex = (JSONObject) results.get(0);
				JSONObject location = (JSONObject) objAtIndex.get("geometry");
				JSONObject location2 = (JSONObject) location.get("location");
				latitude = (String) location2.get("lat").toString();
				longitude = (String) location2.get("lng").toString();
				JSONArray smallImageUrls = (JSONArray) objAtIndex.get("address_components");
				for (Object object2 : smallImageUrls) {
					JSONObject childJSONObject = (JSONObject) object2;
					JSONArray inner = (JSONArray) childJSONObject.get("types");
					for (Object object3 : inner) {
						String kkk = (String) object3;
						if (kkk.equals("country")) {
							countryName = (String) childJSONObject.get("long_name");
							countryCode = (String) childJSONObject.get("short_name");
						}
						if (kkk.equals("postal_code")) {
							postalCode = (String) childJSONObject.get("long_name");
						}

						if (kkk.equals("locality")) {
							city = (String) childJSONObject.get("long_name");
						}

						if (kkk.equals("postal_town")) {
							String tempCity = (String) childJSONObject.get("long_name");
							if (tempCity.contains(",")) {
								String[] cities = tempCity.split(",");
								city = cities[1];
							} else {
								city = tempCity;
							}
						}

					}
				}
			}

		} catch (Exception e) {
			logger.warn("Retriving Europe Company GeoLocation from google has faild !!");
		}

		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);

		return new GeoLocation(timeZoneOffset, countryCode, countryName, region, city, postalCode, latitude, longitude, GeoApiResolver.GOOGLE);
	}

	/**
	 * Retrieving US GeoLocation from google API
	 */
	public  GeoLocation getGoggleCompanyGeoLocationUS(String search) {
		Integer timeZoneOffset = 0;
		String region = null;
		String countryName = null;
		String countryCode = null;
		String postalCode = null;
		String output = null;
		String latitude = null;
		String longitude = null;
		String city = null;
		JSONParser parser = new JSONParser();
		try {
			search = search.replace(" ", "%20");
			Client client = Client.create();
			WebResource webResource = client.resource(GeoResolverConstants.GOOGLE_ADDRESS_API_PREFIX + search + GeoResolverConstants.GOOGLE_API_KEY);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			Object obj = parser.parse(output);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray results = (JSONArray) jsonObject.get("results");
			for (Object object : results) {
				JSONObject objAtIndex = (JSONObject) results.get(0);
				JSONObject location = (JSONObject) objAtIndex.get("geometry");
				JSONObject location2 = (JSONObject) location.get("location");
				latitude = (String) location2.get("lat").toString();
				longitude = (String) location2.get("lng").toString();
				JSONArray smallImageUrls = (JSONArray) objAtIndex.get("address_components");
				for (Object object2 : smallImageUrls) {
					JSONObject childJSONObject = (JSONObject) object2;
					JSONArray inner = (JSONArray) childJSONObject.get("types");
					for (Object object3 : inner) {
						String kkk = (String) object3;
						if (kkk.equals("country")) {

							countryCode = (String) childJSONObject.get("short_name");
						}
						if (kkk.equals("postal_code")) {
							postalCode = (String) childJSONObject.get("long_name");
						}

						if (kkk.equals("locality")) {
							city = (String) childJSONObject.get("long_name");
						}

						if (kkk.equals("administrative_area_level_2")) {
							region = (String) childJSONObject.get("long_name");
						}
						if (kkk.equals("administrative_area_level_1")) {
							countryName = (String) childJSONObject.get("long_name");
						}

					}
				}
			}

		} catch (Exception e) {
			logger.warn("Retriving US Company GeoLocation from google has faild !!");
		}

		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);

		return new GeoLocation(timeZoneOffset, countryCode, countryName, region, city, postalCode, latitude, longitude, GeoApiResolver.GOOGLE);
	}

	/**
	 * Http client method for consuming 'OPEN STREET MAP' geo-location REST services
	 * @param searchWord
	 * @return {@link GeoLocation}
	 */
	public  GeoLocation getOpenStreetMapGeoLocation(String searchWord) {
		Integer timeZoneOffset = 0;
		String output = null;
		String region = null;
		String countryName = null;
		String countryCode = null;
		String postalCode = null;
		String latitude = null;
		String longitude = null;
		String city = null;
		JSONParser parser = new JSONParser();
		searchWord = searchWord.replaceAll("[\\s|\\u00A0]+", "%20");
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(GeoResolverConstants.OPEN_STREET_MAP_ADDRESS_API_PREFIX + searchWord + GeoResolverConstants.OPEN_STREET_MAP_ADDRESS_API_SUFFIX);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				return new GeoLocation();
			}
			output = response.getEntity(String.class);

			Object obj = parser.parse(output);
			JSONArray results = (JSONArray) obj;
			for (Object object : results) {
				JSONObject objAtIndex = (JSONObject) results.get(0);

				latitude = (String) objAtIndex.get("lat");
				longitude = (String) objAtIndex.get("lon");

				JSONObject addressObject = (JSONObject) objAtIndex.get("address");

				countryName = (String) addressObject.get("country");
				countryCode = (String) addressObject.get("country_code");
				String town = (String) addressObject.get("town");
				String road = (String) addressObject.get("road");
				city = (String) addressObject.get("city");
				region = (String) addressObject.get("state_district");
				String county = (String) addressObject.get("county");
				postalCode = (String) addressObject.get("postcode");
				String house_number = (String) addressObject.get("v");
				String state = (String) addressObject.get("state");
				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		timeZoneOffset = timezone.getOffset(Calendar.ZONE_OFFSET);
		return new GeoLocation(timeZoneOffset, countryCode, countryName, region, city, postalCode, latitude, longitude, GeoApiResolver.OPEN_STREET_MAP);

	}
	

}
