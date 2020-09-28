package com.gollgi.resolver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class represents the Error response for the client in
 * case of  authentication failure 
 * 
 * @author Hoffman
 *
 */
@JsonInclude(Include.NON_NULL)
public class WebErrorResponse extends WebResponse {

	@JsonProperty("error_message")
	private String errorMessage;
	@JsonProperty("additional_info")
	private String additionalInfo;
	@JsonProperty("time_stamp")
	private Long timeStamp;

	public WebErrorResponse(String errorMessage, String additionalInfo) {
		this.errorMessage = errorMessage;
		this.additionalInfo = additionalInfo;
		this.timeStamp = System.currentTimeMillis();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

}
