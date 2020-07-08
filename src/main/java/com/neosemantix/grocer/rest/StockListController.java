// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.service.RegistrationService;
import com.neosemantix.grocer.service.StockListService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/mystore/stocklist")
public class StockListController {

	private final StockListService stockListService;

	public StockListController(@Autowired StockListService sl) {
		this.stockListService = sl;
	}
	
	@GetMapping("/{item}")
	public Flux<Grocer> grocersSellingItem(@PathVariable String item) {
		if (RegistrationService.LocationCache.contains(item)) {
			// it is actually location
			return this.stockListService.findGrocersByLocation(item);
		} else {
			return stockListService.findSellingItem(item);
		}
	}
}
