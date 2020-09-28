package com.gollgi.resolver.util;

import com.gollgi.resolver.app.objects.GeoApiResolver;

/**
 * Geo Resolver engine helper methods
 * @author oreng
 *
 */
public abstract class GeoLocationUtils {	


	public static boolean isResolverSupported(String resolver) {
		resolver = resolver.toUpperCase();
		try {
			GeoApiResolver geoResolver = GeoApiResolver.valueOf(resolver);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
