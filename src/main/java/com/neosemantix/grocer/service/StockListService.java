// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.repo.GrocerRepository;

import reactor.core.publisher.Flux;

/**
 * Service layer serving stock listing related calls:
 * - find grocers with the given location
 * - find grocers which sell the specified item
 * - return all grocers in the system.
 */
@Service
public class StockListService {

	private final GrocerRepository grocerRepository;
	
	public StockListService(@Autowired GrocerRepository gr) {
		this.grocerRepository = gr;
	}
	
	public Flux<Grocer> findGrocersByLocation(String location){
		return grocerRepository.findByLocation(location);
	}
	
	public Flux<Grocer> getAll(){
		return this.grocerRepository.findAll();
	}
	
	public Flux<Grocer> findSellingItem(String item){
		return this.grocerRepository.findSellingItem(item);
	}

}
