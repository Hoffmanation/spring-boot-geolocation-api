package com.gollgi.resolver.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gollgi.resolver.app.objects.GeoApiResolver;

/**
 * The persistent class for the geoLocation database table.
 * 
 */
@Entity
@Table(name = "geolocation")
public class GeoLocation implements Serializable {
	private static final long serialVersionUID = -3605682071955827721L;

	@Id
	@Column(name = "geolocation_id")
	private UUID geoLocationId = UUID.randomUUID();

	@Column(name = "timezone_offset")
	private Integer timeZone;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "region")
	private String region;

	@Column(name = "city")
	private String city;

	@Column(name = "postalCode")
	private String postalCode;

	@Column(name = "latitude")
	private String latitude;

	@Column(name = "longitude")
	private String longitude;

	@Enumerated(EnumType.STRING)
	@Column(name = "resolver")
	private GeoApiResolver resolver;

	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public GeoLocation() {

	}

	public GeoLocation(Integer timeZone, String countryCode, String countryName, String regeon, String city,
			String postalCode, String latitude, String longitude , GeoApiResolver resolver) {
		super();
		this.timeZone = timeZone;
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.region = regeon;
		this.city = city;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.resolver= resolver ;
	}


	public UUID getgeoLocationId() {
		return geoLocationId;
	}

	public void setgeoLocationId(UUID geoLocationId) {
		this.geoLocationId = geoLocationId;
	}

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public GeoApiResolver getResolver() {
		return resolver;
	}

	public void setResolver(GeoApiResolver resolver) {
		this.resolver = resolver;
	}

	@JsonIgnore
	public boolean isEmpty() {
		if (null == this || null == this.latitude ||  null == this.longitude) {
			return true;
		}
		return false;
	}
	
	@JsonIgnore
	public boolean isPresentable() {
		if (null != this && null != this.latitude && null != this.longitude && null != this.countryName && null!=this.city && null!=this.region) {
			return true;
		}
		return false;
	}
	


	@Override
	public String toString() {
		return "geoLocation [geoLocationId=" + geoLocationId + ", timeZone=" + timeZone + ", countryCode=" + countryCode + ", countryName=" + countryName + ", regeon=" + region + ", city=" + city + ", postalCode=" + postalCode + ", latitude="
				+ latitude + ", longitude=" + longitude + ", resolver=" + resolver.toString() + "]";
	}


}
