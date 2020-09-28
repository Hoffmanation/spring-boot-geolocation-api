package com.gollgi.resolver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the Authentication response for the client in
 * case of  successful  authentication
 * 
 * @author Hoffman
 *
 */
@JsonInclude(Include.NON_NULL)
public class WebAuthenticationResponse extends WebResponse{

	 @JsonProperty("message")
	private String message;
	 @JsonProperty("additional_info")
	private String additionalInfo;
	 @JsonProperty("time_stamp")
	private Long timeStamp;
	 @JsonProperty("expiration")
	private Long exp;
	 @JsonProperty("access_token")
	private String accessToken;
	 @JsonProperty("granted")
	private Boolean granted ;
	 @JsonProperty("version")
	private String version ;
	
	public WebAuthenticationResponse(String message, String additionalInfo, Long exp, String accessToken, Boolean granted, String version) {
		super();
		this.message = message;
		this.additionalInfo = additionalInfo;
		this.timeStamp = System.currentTimeMillis();
		this.exp = exp;
		this.accessToken = accessToken;
		this.granted = granted;
		this.version = version;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Boolean getGranted() {
		return granted;
	}

	public void setGranted(Boolean granted) {
		this.granted = granted;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "AuthenticationResponse [message=" + message + ", additionalInfo=" + additionalInfo + ", timeStamp=" + timeStamp + ", exp=" + exp + ", accessToken=" + accessToken + ", granted="
				+ granted + ", version=" + version + "]";
	}
	
	
	
	



}
