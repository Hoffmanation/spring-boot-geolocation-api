package com.gollgi.resolver.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gollgi.resolver.app.objects.GeoApiResolver;
import com.gollgi.resolver.entity.GeoLocation;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * Geo Resolver engine helper methods
 * 
 * @author Hoffman
 *
 */
@Service
public class GeoLocationUtils {

	@Autowired
	private Client httpClient;
	
	@Autowired
	private JSONParser parser ;
	
	
	/**
	 * In case an error occurred - we still want to return an empty object
	 * @param resolvers 
	 * @return new {@link GeoLocation}
	 */
	public GeoLocation getDefaultGeoLocation(List<GeoApiResolver> resolvers) {
		return new GeoLocation(resolvers);
	}
	
	/**
	 * Deserialize HTTP response into {@link JSONObject} object
	 * @param jsonString 
	 * @return Object extends JSONAware
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends JSONAware> T getJsonObject(String jsonString) throws ParseException {
		return (T) parser.parse(jsonString);
	}


	/**
	 * Return http-builder ready for HTTP invocations
	 * @param endpoint
	 * @return
	 */
	public Builder getHttpClient(String endpoint) {
		return httpClient.resource(endpoint).accept("application/json");
	}

	/**
	 * Validate that requested resolver exists in the system
	 * @param resolver
	 * @return
	 */
	public boolean isResolverSupported(String resolver) {
		resolver = resolver.toUpperCase();
		try {
			GeoApiResolver.valueOf(resolver);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 	 Validate that REST response contain valid representation of the expected Geo-Location data
	 * @param response
	 * @param output
	 * @return
	 */
	public boolean isResponseValid(ClientResponse response, String output) {
		boolean containResponse = response.getStatus() == 200 && !StringUtils.isEmpty(output) &&  !output.equals("[]") ;
		if (!containResponse) {
			return false ;
		}
		try {
			this.getJsonObject(output) ;
		} catch (ParseException e) {
			return false ;
		}
		return true ;
	}
	

}
