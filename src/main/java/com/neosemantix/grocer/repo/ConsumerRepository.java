// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
package com.neosemantix.grocer.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.neosemantix.grocer.model.Consumer;

/**
 * Reactive MongoDB Repository to manage persistence of Consumer entity.
 */
public interface ConsumerRepository extends ReactiveMongoRepository<Consumer, String> {

}
