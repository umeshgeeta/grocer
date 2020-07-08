// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * Custom error handler which prevents sending the entire stack to the client.
 * For GET calls, when URI is valid but there is not data for the given 
 * parameter (location or item); we get empty response. If URI is not known 
 * one, 404 is returned. For POST calls, we get 500 error code; say when the
 * payload is different than expected. 
 */
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	/**
	 * @param errorAttributes
	 * @param resourceProperties
	 * @param applicationContext
	 * @param configurer
	 */
	public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, 
			ResourceProperties resourceProperties,
			ApplicationContext applicationContext, 
			ServerCodecConfigurer configurer) {
		super(errorAttributes, resourceProperties, applicationContext);
		this.setMessageWriters(configurer.getWriters());
	}

	@Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
          RequestPredicates.all(), this::renderErrorResponse);
    }
 
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
       Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
       return ServerResponse.status(HttpStatus.BAD_REQUEST)
         .contentType(MediaType.APPLICATION_JSON_UTF8)
         .body(BodyInserters.fromObject(errorPropertiesMap));
    }
}