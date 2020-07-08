// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neosemantix.grocer.model.Consumer;
import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.repo.ConsumerRepository;
import com.neosemantix.grocer.repo.GrocerRepository;

import reactor.core.publisher.Mono;

@Service
public class RegistrationService {
	
	private final ConsumerRepository consumerRepository;
	private final GrocerRepository grocerRepository;

	public RegistrationService(@Autowired ConsumerRepository cr, 
			@Autowired GrocerRepository gr) {
		this.consumerRepository = cr;
		this.grocerRepository = gr;
	}
	
	public String registerConsumer(Consumer cons1) {
		if (cons1 != null && cons1.getName() != null && !cons1.getName().isEmpty() &&
				cons1.getLocation() != null && !cons1.getLocation().isEmpty()) {
			Mono<Consumer> mc = consumerRepository.save(cons1);
			return mc.block().getId();
		} else {
			throw new IllegalArgumentException("Invalid consumer");
		}
	}
	
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
	
	public Mono<Consumer> findConsumerById(String cid){
		 return consumerRepository.findById(cid);
	}

}
