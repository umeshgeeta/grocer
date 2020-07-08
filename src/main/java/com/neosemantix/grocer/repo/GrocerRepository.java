// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.neosemantix.grocer.model.Grocer;

import reactor.core.publisher.Flux;

public interface GrocerRepository extends ReactiveMongoRepository<Grocer, String> {

	@Query("{ 'location': ?0 }")
	Flux<Grocer> findByLocation(String location);
}
