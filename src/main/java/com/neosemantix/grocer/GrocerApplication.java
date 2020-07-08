package com.neosemantix.grocer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Common entry point for the application when run as a standalong 
 * Java Application.
 */
@SpringBootApplication
public class GrocerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrocerApplication.class, args);
	}

}
