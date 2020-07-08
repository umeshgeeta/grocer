// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Consumer entity.
 */
@Document 
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Consumer {

    @Id 
    private String id;

    private String name;
    
    private String location;
}