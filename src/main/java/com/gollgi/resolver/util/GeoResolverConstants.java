package com.gollgi.resolver.util;

public interface GeoResolverConstants {
	
	public static final String UNSUPPORTED_RESOLVER = "Resolver Not Supported" ;
	public static final String API_INVOCATION_FAILURE= "invocation failure" ;
	
	// Goggle Api
	public static final String GOOGLE_ADDRESS_API_PREFIX = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	public static final String GOOGLE_LET_LONG_API_PREFIX = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
	public static final String GOOGLE_API_KEY = "&key=AIzaSyAQ0RWu-nLXgWEDN3-igne6CH_QikfYZU0";
	
	// Open Street Map
	public static final String OPEN_STREET_MAP_ADDRESS_API_PREFIX = "https://nominatim.openstreetmap.org/search?q=";
	public static final String OPEN_STREET_MAP_ADDRESS_API_SUFFIX = "&format=json&polygon=1&addressdetails=1";

	// Bing Api
	public static final String BING_ADDRESS_API_PREFIX = "http://dev.virtualearth.net/REST/v1/Locations?addressLine=";
	public static final String BING_ADDRESS_API_SUFFIX  = "&key=AvRUqepvfjLDKckkLmuo-6TdVvY6ClYABK8f5a5eGvj70qHtLle8p_g1OdIKIJv4";
	public static final String BING_Q_API_PREFIX = "http://dev.virtualearth.net/REST/v1/Locations?q=";
	
	//Here Api
	public static final String HERE_ADDRESS_API_PREFIX = "https://geocoder.api.here.com/6.2/geocode.json?app_id=2oCu3WouOb2MNToaZmT4&app_code=HnsR_9Rg-uHPHfctAZd8YQ&searchtext=";

}
