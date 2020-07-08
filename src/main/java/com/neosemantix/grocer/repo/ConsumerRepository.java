// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.neosemantix.grocer.model.Consumer;

public interface ConsumerRepository extends ReactiveMongoRepository<Consumer, String> {

}
