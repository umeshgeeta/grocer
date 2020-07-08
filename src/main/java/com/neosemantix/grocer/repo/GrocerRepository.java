// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.neosemantix.grocer.model.Grocer;

import reactor.core.publisher.Flux;

/**
 * Reactive MongoDB Repository object to manager persistence of Grocer entity.
 */
public interface GrocerRepository extends ReactiveMongoRepository<Grocer, String> {

	@Query("{ 'location': ?0 }")
	Flux<Grocer> findByLocation(String location);
	
	/**
	 * Finds grocer documents for which in their 'items on sale' contains the
	 * specific item passed. We use 'in' operator specifically for this query.
	 * 
	 * Ref.: https://docs.mongodb.com/manual/reference/operator/query/in/
	 * 
	 * @param item
	 * @return Flux<Grocer> who sell the passed item.
	 */
	@Query("{ 'itemsOnSale': { $in: [?0]} }")
	Flux<Grocer> findSellingItem(String item);
}
