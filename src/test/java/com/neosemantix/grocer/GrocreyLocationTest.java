// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.service.RegistrationService;
import com.neosemantix.grocer.service.StockListService;

import reactor.core.publisher.Flux;

@DataMongoTest 
@Import({RegistrationService.class, StockListService.class})
public class GrocreyLocationTest {

	private final RegistrationService regService;
	private final StockListService stockListService;
	
	public GrocreyLocationTest(@Autowired RegistrationService rs,
			@Autowired StockListService sl) {
		this.regService = rs;
		this.stockListService = sl;
	}

	@Test
	public void gorcersByLocationAndSellingItems() {
		List<Grocer> grcs = SampleGroceryDistribution.groceryList;
		for(Grocer g: grcs) {
			String gid = regService.registerGrocer(g);
			System.out.println("Saved grocer successfully with id " + gid);
		}
		Assert.isTrue(validateGrocersByLocation(SampleGroceryDistribution.Loc_Saratoga, 2), "Grocery validation in " + SampleGroceryDistribution.Loc_Saratoga + " failed.");;
		Assert.isTrue(validateGrocersByLocation(SampleGroceryDistribution.Loc_Cupertino, 1), "Grocery validation in " + SampleGroceryDistribution.Loc_Cupertino + " failed.");;
		Assert.isTrue(validateGrocersByLocation(SampleGroceryDistribution.Loc_SanJose, 2), "Grocery validation in " + SampleGroceryDistribution.Loc_SanJose + " failed.");;
	
		System.out.println("Validated locationwise stores ");
		
		Flux<Grocer> grocers = stockListService.findSellingItem("Prawn");
		// We expect Ranch 99 - Cupertino & San Jose - to sell this item.	
		// We validate these two grocers
		grocers.subscribe(store -> {
			Assert.isTrue(store.getName().equals("Ranch 99"), "Expected grocer is Ranch 99 only.");
			String loc = store.getLocation();
			Assert.isTrue(loc.equals(SampleGroceryDistribution.Loc_Cupertino) || 
					loc.equals(SampleGroceryDistribution.Loc_SanJose), 
					"Store is expected in Cupertino or San Jose only.");
			Assert.isTrue(store.getItemsOnSale().contains("Prawn"), "The store does not sell Prawns!");
			System.out.println("You can buy Prawns at: " + store);
		});
		System.out.println("Validated that exactly two Ranch 99 sells Prawns, we are good!");
	}
	
	private boolean validateGrocersByLocation(String location, int howManyExpected) {
		Flux<Grocer> grocers = stockListService.findGrocersByLocation(location);
		Iterator<Grocer> it = grocers.toIterable().iterator();
		int count = 0;
		while (it.hasNext()) {
			Grocer cupg = it.next();
			Assert.notNull(cupg, "Grocer in " + location + " expected to be non-null");
			Assert.isTrue(cupg.getLocation().equals(location), "Location expected " + location +", but is: " + cupg.getLocation());
			count++;
		}
		Assert.isTrue(count == howManyExpected, "We should have exactly " + howManyExpected + " store(s) in " + location);
		
		return true;
	}
}
