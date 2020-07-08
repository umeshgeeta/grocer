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

/**
 * REST API Controller for to get grocers in the given location or who sells
 * the specified item. 
 * 
 * From URI perspective, since both "/mystore/stocklist/{item}" and 
 * "/mystore/stocklist/{location}" are same; we only have one single end point.
 * What we do is we check the parameter whether it is location or not. If it a
 * valid location, we take it as the later API call and use service method to 
 * find and return grocers in the passed location. If it is not a valid location.
 * we treat it as an item and try to find list of grocers which sell that item 
 * and return that list.
 * 
 * Determination of valid location is done based on an internal cache of location
 * strings which is populated as we register new grocery stores or consumers.
 */
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
