package com.gollgi.resolver.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "ISO")
	private String ISO;

	@Column(name = "resolvers")
	@Enumerated
	@ElementCollection(targetClass = GeoApiResolver.class)
	private List<GeoApiResolver> resolvers;

	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GeoLocation() {

	}

	public GeoLocation(Integer timeZone, String countryCode, String countryName, String region, String city,
			String postalCode, String latitude, String longitude, String displayName, List<GeoApiResolver> resolvers,
			String address, String ISO) {
		super();
		this.timeZone = timeZone;
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.region = region;
		this.city = city;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.resolvers = resolvers;
		this.displayName = displayName;
		this.address = address;
		this.ISO = ISO;
	}

	public GeoLocation(List<GeoApiResolver> resolvers) {
		this.resolvers = resolvers ;
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

	public List<GeoApiResolver> getResolvers() {
		return resolvers;
	}

	public void setResolver(List<GeoApiResolver> resolvers) {
		this.resolvers = resolvers;
	}

	public UUID getGeoLocationId() {
		return geoLocationId;
	}

	public void setGeoLocationId(UUID geoLocationId) {
		this.geoLocationId = geoLocationId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setResolvers(List<GeoApiResolver> resolvers) {
		this.resolvers = resolvers;
	}

	public String getISO() {
		return ISO;
	}

	public void setISO(String iSO) {
		ISO = iSO;
	}

	@JsonIgnore
	public boolean isEmpty() {
		if (null == this || null == this.latitude || null == this.longitude) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean isPresentable() {
		if (null != this && null != this.latitude && null != this.longitude && null != this.countryName
				&& null != this.city && null != this.region) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "GeoLocation [geoLocationId=" + geoLocationId + ", timeZone=" + timeZone + ", countryCode=" + countryCode
				+ ", countryName=" + countryName + ", region=" + region + ", city=" + city + ", postalCode="
				+ postalCode + ", latitude=" + latitude + ", longitude=" + longitude + ", displayName=" + displayName
				+ ", ISO=" + ISO + ", resolvers=" + resolvers + ", address=" + address + "]";
	}

}
