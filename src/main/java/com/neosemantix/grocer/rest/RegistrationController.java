// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.rest;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neosemantix.grocer.model.Consumer;
import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.service.RegistrationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mystore/register")
public class RegistrationController {

	private final RegistrationService registrationService;

	public RegistrationController(@Autowired RegistrationService rs) {
		this.registrationService = rs;
	}
	
	@PostMapping("/consumer")
	public Mono<Consumer> registerConsumer(@RequestBody Consumer consumer) {
		return this.registrationService.regConsumer(consumer);
	}
	
	@PostMapping("/grocer")
	public Mono<Grocer> registerGrocer(@RequestBody Grocer grocer) {
		return this.registrationService.regGrocer(grocer);
	}

}
