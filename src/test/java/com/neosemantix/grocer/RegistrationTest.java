package com.neosemantix.grocer;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import com.neosemantix.grocer.model.Consumer;
import com.neosemantix.grocer.model.Grocer;
import com.neosemantix.grocer.service.RegistrationService;
import com.neosemantix.grocer.service.StockListService;

//@SpringBootTest
@DataMongoTest 
@Import({RegistrationService.class, StockListService.class})
class RegistrationTest {
	
	private final RegistrationService regService;
	private final StockListService stockListService;
	
	public RegistrationTest(@Autowired RegistrationService rs,
			@Autowired StockListService sl) {
		this.regService = rs;
		this.stockListService = sl;
	}
	
	@Test
	public void registerConsumer() {
		Consumer s = new Consumer();
		String name = "John Smith";
		String loc = "Saratoga";
		s.setName(name);
		s.setLocation(loc);
		String sid = regService.registerConsumer(s);
		Assert.notNull(sid, sid);
		System.out.println("Save consumer successfully with id " + sid);
		
		Consumer retrieved = regService.findConsumerById(sid).block();
		Assert.isTrue(retrieved.getId().equals(sid), "Id not matched");
		Assert.isTrue(retrieved.getName().equals(name), "Name not matched");
		Assert.isTrue(retrieved.getLocation().equals(loc), "Location not matched");
		System.out.println("Retrieved consumer matches: " + retrieved);
	}

	@Test
	public void registerGrocer() {
		List<Grocer> grcs = SampleGroceryDistribution.groceryList;
		for(Grocer g: grcs) {
			String gid = regService.registerGrocer(g);
			System.out.println("Saved grocer successfully with id " + gid);
		}
		stockListService.getAll().subscribe(grocer -> {
			Assert.isTrue(SampleGroceryDistribution.validateGrocer(grocer), "Grocer not matched: " + grocer);
			System.out.println("Validated grocer: " + grocer);			
		});
	}
}
