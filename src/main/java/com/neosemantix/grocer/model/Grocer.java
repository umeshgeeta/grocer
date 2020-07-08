// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Grocer entity.
 */
@Document 
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Grocer {

    @Id 
    private String id;

    private String name;
    
    private String location;
    
    private Set<String> itemsOnSale;

}
