// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.neosemantix.grocer.model.Grocer;

public class SampleGroceryDistribution {
	
	public static final String Loc_Saratoga = "Saratoga";
	public static final String Loc_Cupertino = "Cupertino";
	public static final String Loc_SanJose = "San Jose";

	private static Map<Integer, String> StockList = new HashMap<>();
	private static Map<String, Integer> ReverseStockList = new HashMap<>();
	private static Map<Integer, Short> PowerOfTwo = new HashMap<>();
	
	private static String[][] distribution = {
			{Loc_Saratoga,	"Nob Hill",		"110"},
			{Loc_Saratoga,	"Safeway",		"230"},
			{Loc_Cupertino,	"Ranch 99",		"158"},
			{Loc_SanJose,	"Ranch 99",		"94"},
			{Loc_SanJose,	"Safeway",		"174"}
	};
	
	public static List<Grocer> groceryList;
	
	static {
		StockList.put(1, "Banana");
		ReverseStockList.put("Banana", 1);
		
		StockList.put(2, "Milk");
		ReverseStockList.put("Milk", 2);
		
		StockList.put(3, "Salmon");
		ReverseStockList.put("Salmon", 3);
		
		StockList.put(4, "Prawn");
		ReverseStockList.put("Prawn", 4);
		
		StockList.put(5, "Frozen Pizza");
		ReverseStockList.put("Frozen Pizza", 5);
		
		StockList.put(6, "Organic Milk");
		ReverseStockList.put("Organic Milk", 6);
		
		StockList.put(7, "Sourdough Bread");
		ReverseStockList.put("Sourdough Bread", 7);
		
		// All because Java does not have easy power operator....
		PowerOfTwo.put(1, (short) 2);
		PowerOfTwo.put(2, (short) 4);
		PowerOfTwo.put(3, (short) 8);
		PowerOfTwo.put(4, (short) 16);
		PowerOfTwo.put(5, (short) 32);
		PowerOfTwo.put(6, (short) 64);
		PowerOfTwo.put(7, (short) 128);
		
		groceryList = buildDistribuion();
	}
	
/*
 * We are going with following distribution of stock items over different Gorcers at different locations.
 * When there is no 'No' for a stock item, we assume that it is available with the gocer. For example,
 * Banana and Milk are available with all grocers.


Location		Grocer			Banana		Milk		Salmon		Prawn		Frozen Pizza	Organic Milk	Sourdough Bread
-------------------------------------------------------------------------------------------------------------------------------

Saratoga		Nob Hill											No 											No

				Safeway									No 			No

Cupertino		Ranch 99														No 				No

San Jose		Ranch 99														No 								No

				Safeway												No 							No 

This translates into:

		result.add(buildGrocer("Saratoga",	"Nob Hill",	1, 2, 3, 5, 6));
		result.add(buildGrocer("Saratoga",	"Safeway", 	1, 2, 5, 6, 7));
		result.add(buildGrocer("Cupertino",	"Ranch 99", 1, 2, 3, 4, 7));
		result.add(buildGrocer("San Jose",	"Ranch 99", 1, 2, 3, 4, 6));
		result.add(buildGrocer("San Jose",	"Safeway",	1, 2, 3, 5, 7));

*/
	
	private static short encodeStockList(int...items) {
		short result = 0;
		for(int i: items) {
			result = (short) (result | PowerOfTwo.get(i));
		}
		return result;
	}
	
	private static Set<String> decodeStockList(short s){
		Set<String> result = new HashSet<>();
		for(int i=1; i<8; i++) {
			short m = (short) (s & PowerOfTwo.get(i));
			if (m > 0) {
				result.add(StockList.get(i));
			}
		}
		return result;
	}
	
	private static String encodeStockList(Set<String> items) {
		String result = "";
		int[] ia = new int[5];	// we know the size is fixed...
		int j = 0;
		for(String item: items) {
			ia[j++] = ReverseStockList.get(item);
		}
		return Short.toString(encodeStockList(ia));
	}
	
	private static List<Grocer> buildDistribuion(){
		List<Grocer> result = new ArrayList<Grocer>();
		
		result.add(buildGrocer(distribution[0][0], distribution[0][1], decodeStockList(Short.parseShort(distribution[0][2]))));
		result.add(buildGrocer(distribution[1][0], distribution[1][1], decodeStockList(Short.parseShort(distribution[1][2]))));
		result.add(buildGrocer(distribution[2][0], distribution[2][1], decodeStockList(Short.parseShort(distribution[2][2]))));
		result.add(buildGrocer(distribution[3][0], distribution[3][1], decodeStockList(Short.parseShort(distribution[3][2]))));
		result.add(buildGrocer(distribution[4][0], distribution[4][1], decodeStockList(Short.parseShort(distribution[4][2]))));
		
		return result;
	}

	private static Grocer buildGrocer(String location, String name, Set<String> items) {
		Grocer grc = new Grocer();
		grc.setName(name);
		grc.setLocation(location);
		grc.setItemsOnSale(items);
		return grc;
	}
	
	public static boolean validateGrocer(Grocer g) {
		boolean result = false;
		
		if (g != null && g.getId() != null && !g.getId().isEmpty()) {
			
			String location = g.getLocation();
			String grocerName = g.getName();
			
			switch (location) {
			
			case Loc_Saratoga:
				
				switch(grocerName) {
				case "Nob Hill":
					if (encodeStockList(g.getItemsOnSale()).equals(distribution[0][2])) {
						result = true;
					}
					break;
					
				case "Safeway":
					if (encodeStockList(g.getItemsOnSale()).equals(distribution[1][2])) {
						result = true;
					}
					break;
				}
				break;
				
				
			case Loc_Cupertino:
				if (grocerName.equals("Ranch 99")) {
					if (encodeStockList(g.getItemsOnSale()).equals(distribution[2][2])) {
						result = true;
					}
				}
				break;
				
				
			case Loc_SanJose:
				
				switch(grocerName) {
				case "Ranch 99":
					if (encodeStockList(g.getItemsOnSale()).equals(distribution[3][2])) {
						result = true;
					}
					break;
					
				case "Safeway":
					if (encodeStockList(g.getItemsOnSale()).equals(distribution[4][2])) {
						result = true;
					}
					break;
				}
				break;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		// Testing how encoding and decoding of stock list works:
		
		short s1 = encodeStockList(1, 2, 3, 5, 6);		// 110
		short s2 = encodeStockList(1, 2, 5, 6, 7);		// 230
		short s3 = encodeStockList(1, 2, 3, 4, 7);		// 158
		short s4 = encodeStockList(1, 2, 3, 4, 6);		// 94
		short s5 = encodeStockList(1, 2, 3, 5, 7);		// 174
		
		System.out.println(s1 + " " + s2 + " " + s3 + " " + s4 + " "+ s5);
		
		System.out.println(decodeStockList(s1));
		System.out.println(decodeStockList(s2));
		System.out.println(decodeStockList(s3));
		System.out.println(decodeStockList(s4));
		System.out.println(decodeStockList(s5));
		
		List<Grocer> grcs = SampleGroceryDistribution.groceryList;
		for(Grocer g: grcs) {
			System.out.println(g);
		}

	}
}
