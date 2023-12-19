package com.gollgi.resolver.config;

import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gollgi.resolver.entity.GeoLocation;
import com.sun.jersey.api.client.Client;

@Configuration
public class BeanConfigProvider {

	/**
	 * Register the jersey http client
	 * @return
	 */
	@Bean
	public Client createJerseyClient() {
		return Client.create();
	}
	
	@Bean
	public  JSONParser createJsonParser() {
		return new JSONParser();
	}
}
