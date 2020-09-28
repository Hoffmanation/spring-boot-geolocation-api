package com.gollgi.resolver.rest;


import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gollgi.resolver.entity.GeoLocation;
import com.gollgi.resolver.service.GeoLocationManager;



/**
 *A collection of  Rest API endpoints which will resolve a {@link GeoLocation} by a given attributes
 * 
 * @author Haffman
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class GeoLocationController {

@Autowired
private GeoLocationManager geoLocationManager ;

/**
 *  A REST API endpoint for invoking a specific geo-location provider in order to retrieve a {@link GeoLocation} object
 * @param address - The desired address from which the resolver will return the {@link GeoLocation}
 * @param resolverName - The name of the geo-location provider (resolver)
 * @return {@link Response} - An HTTP response containing the {@link GeoLocation} object
 */
	@RequestMapping(path = "secured/get-location-by-resolver/{address}/{resolver}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response getGeoLocationByResolver(@PathVariable("address") String address, @PathVariable("resolver") String resolver) {
		return geoLocationManager.getGeoLocationByResolver(address,resolver) ;
	}
	
	/**
	 * A REST API endpoint for invoking a random  geo-location provider in order to retrieve a {@link GeoLocation} object
	 * @param address - The desired address from which the resolver will return the {@link GeoLocation}
	 * @param resolverName - The name of the geo-location provider (resolver)
	 * @return {@link Response} - An HTTP response containing the {@link GeoLocation} object
	 */
	@RequestMapping(path = "secured/get-location", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response getGeoLocation(@RequestParam(name="search") String search) {
		return geoLocationManager.getGeoLocation(search); 
	}


}