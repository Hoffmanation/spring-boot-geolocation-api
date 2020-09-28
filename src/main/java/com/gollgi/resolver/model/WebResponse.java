package com.gollgi.resolver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The root class to extend for the Rest Api's Json response
 * @author Hoffman
 *
 */
@JsonInclude(Include.NON_NULL)
public abstract class WebResponse {

}
