// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neosemantix.grocer.model.Consumer;
import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.repo.ConsumerRepository;
import com.neosemantix.grocer.repo.GrocerRepository;

import reactor.core.publisher.Mono;

/**
 * Service which registers new consumer and new grocer. It has two flavors of
 * each functionality:
 * - one for testing where we block on the DB returned Mono or Flux and then 
 * return the id of the new entity added and
 * - the other one for production where we cannot block (WebFlux gives error)
 * and return the Mono or Flux to the Web Flux which handles streaming entities
 * to the client.
 */
@Service
public class RegistrationService {
	
	public static Set<String> LocationCache = new HashSet<>();
	
	private final ConsumerRepository consumerRepository;
	private final GrocerRepository grocerRepository;

	public RegistrationService(@Autowired ConsumerRepository cr, 
			@Autowired GrocerRepository gr) {
		this.consumerRepository = cr;
		this.grocerRepository = gr;
	}
	
	/**
	 * Used for testing purposes.
	 * 
	 * @param cons1
	 * @return String Id of thenewly registered consumer.
	 */
	public String registerConsumer(Consumer cons1) {
		if (cons1 != null && cons1.getName() != null && !cons1.getName().isEmpty() &&
				cons1.getLocation() != null && !cons1.getLocation().isEmpty()) {
			Mono<Consumer> mc = consumerRepository.save(cons1);
			return mc.block().getId();
		} else {
			throw new IllegalArgumentException("Invalid consumer");
		}
	}
	
	/**
	 * Used for production. Spring-Web-Flux takes care of actually sending the 
	 * consumer to client.
	 * 
	 * @param cons1
	 * @return Mono<Consumer> The published which will emit the newly 
	 * 							added consumer.
	 */
	public Mono<Consumer> regConsumer(Consumer cons1) {
		if (cons1 != null && cons1.getName() != null && !cons1.getName().isEmpty() &&
				cons1.getLocation() != null && !cons1.getLocation().isEmpty()) {
			
			// track location
			LocationCache.add(cons1.getLocation());
			
			return consumerRepository.save(cons1);
		} else {
			throw new IllegalArgumentException("Invalid consumer");
		}
	}
	
	/**
	 * Used for testing purposes.
	 * 
	 * @param grc
	 * @return String Id of the newly added grocer.
	 */
	public String registerGrocer(Grocer grc) {
		if (grc != null && grc.getName() != null && !grc.getName().isEmpty() &&
				grc.getLocation() != null && !grc.getLocation().isEmpty() &&
				grc.getItemsOnSale() != null && !grc.getItemsOnSale().isEmpty() && grc.getItemsOnSale().size() < 6) {
			Mono<Grocer> mg = this.grocerRepository.save(grc);
			return mg.block().getId();
		} else {
			throw new IllegalArgumentException("Invalid grocer");
		}
	}
	
	/**
	 * For production.
	 * 
	 * @param grc
	 * @return Mono<Grocer> Publisher which will emit the newly added grocer.
	 */
	public Mono<Grocer> regGrocer(Grocer grc) {
		if (grc != null && grc.getName() != null && !grc.getName().isEmpty() &&
				grc.getLocation() != null && !grc.getLocation().isEmpty() &&
				grc.getItemsOnSale() != null && !grc.getItemsOnSale().isEmpty() && grc.getItemsOnSale().size() < 6) {
			
			// track location
			LocationCache.add(grc.getLocation());
						 
			
			return this.grocerRepository.save(grc);
		} else {
			throw new IllegalArgumentException("Invalid grocer");
		}
	}
	
	public Mono<Consumer> findConsumerById(String cid){
		 return consumerRepository.findById(cid);
	}

}
