// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import com.neosemantix.grocer.model.Consumer;
import com.neosemantix.grocer.model.Grocer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *	IMPORTANT -				This is not a unit test. It is a functional test.
 *							You need to start the grocer application and running
 *							so that it is able to receive API calls on 
 *							localhost port 8080 and embedded MongoDB has started
 *							behind the server. We then make specific API calls 
 *							and ensure that expected responses are obtained. 
 *							Those responses are 'asserted'. 
 */
public class RestApitTest {
	
	public static void main(String[] ags) {
		RestApitTest rat = new RestApitTest();
		rat.registerConsumer();
		rat.registerGrocer();
		rat.grocersAtLocation();
		rat.itemsAtLocation();
	}
	
	private static MediaType jsonUtf8 = MediaType.APPLICATION_JSON_UTF8;
	private WebClient client;
	
	public RestApitTest() {
		client = WebClient.create("http://localhost:8080");
	}

	@Test
	public void registerConsumer() {
		Consumer s = new Consumer();
		String name = "John Smith";
		String loc = "Saratoga";
		s.setName(name);
		s.setLocation(loc);

		Consumer mv = client.post()
					        .uri("/mystore/register/consumer")
					        .contentType(jsonUtf8)
					        .body(Mono.just(s), Consumer.class)
					        .retrieve()
					        .bodyToMono(Consumer.class)
					        .block();
		
		System.out.println("mv: " + mv);
		
		Assert.notNull(mv, "Returned consumer is expected to be non-null");
		System.out.println("Asserted that returned consumer is not null.");
		
		Assert.notNull(mv.getId(), "Consumer id is expected to be non-null.");
		System.out.println("Asserted that returned consumer has non-null id.");
	}
	
	@Test
	public void registerGrocer() {
		List<Grocer> grcs = SampleGroceryDistribution.groceryList;
		for(Grocer g: grcs) {
			Grocer rg = client.post()
			        .uri("/mystore/register/grocer")
			        .contentType(jsonUtf8)
			        .body(Mono.just(g), Grocer.class)
			        .retrieve()
			        .bodyToMono(Grocer.class)
			        .block();
			
			System.out.println("Returned grocer: " + rg);
			
			Assert.notNull(rg, "Returned grocer is expected to be non-null");
			System.out.println("Asserted that returned grocer is not null.");
			
			Assert.notNull(rg.getId(), "Grocer id is expected to be non-null.");
			System.out.println("Asserted that returned grocer has non-null id.");
		}
	}

	
	@Test
	public void grocersAtLocation() {
				
		Flux<Grocer> mc = client.get()
		.uri("/mystore/stocklist/"+SampleGroceryDistribution.Loc_Cupertino)
        .retrieve()
        .bodyToFlux(Grocer.class);
		
		Grocer rg = mc.blockFirst();
		System.out.println("rg: " + rg);
		
		Assert.isTrue(rg.getLocation().equals(SampleGroceryDistribution.Loc_Cupertino), "Expected location: " + 
				SampleGroceryDistribution.Loc_Cupertino + " found: " + rg.getLocation());
		
		System.out.println("Asserted that location is confirmed.");
	}
	
	@Test
	public void itemsAtLocation() {
				
		Flux<Grocer> mc = client.get()
		.uri("/mystore/stocklist/Prawn")
        .retrieve()
        .bodyToFlux(Grocer.class);
		
		Grocer rg = mc.blockFirst();
		System.out.println("rg: " + rg);
		
		Assert.isTrue(rg.getItemsOnSale().contains("Prawn"), "Did not find Prawn");
		
		System.out.println("Asserted that item is confirmed.");
	}

}
