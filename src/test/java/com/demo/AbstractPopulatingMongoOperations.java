package com.demo;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import com.demo.model.Role;
import com.demo.model.UserAccount;
import com.mongodb.reactivestreams.client.MongoCollection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Populates {@link UserAccount} and {@link Role} sample data via {@link ReactiveMongoOperations}.
 *
 * @author John Thoms
 */
public abstract class AbstractPopulatingMongoOperations extends AbstractUserAccountRoles {	
	@Autowired private ReactiveMongoOperations operations;

    @BeforeEach
    public void setUp() {   	
    	// recreate Roles
		Mono<MongoCollection<Document>> recreateRolesCollection = operations.collectionExists(Role.class)
				.flatMap(exists -> exists ? operations.dropCollection(Role.class) : Mono.just(exists))
				.then(operations.createCollection(Role.class, CollectionOptions.empty()
						.size(1024 * 1024)
						.maxDocuments(10)));
		StepVerifier.create(recreateRolesCollection).expectNextCount(1).verifyComplete();

		Flux<Role> insertRoles = operations.insertAll(Flux.just(roleUser, roleAdmin, roleSuperAdmin).collectList());
		StepVerifier.create(insertRoles).expectNextCount(3).verifyComplete();
		
    	// recreate UserAccounts
		Mono<MongoCollection<Document>> recreateUserAccountCollection = operations.collectionExists(UserAccount.class)
				.flatMap(exists -> exists ? operations.dropCollection(UserAccount.class) : Mono.just(exists))
				.then(operations.createCollection(UserAccount.class, CollectionOptions.empty()
						.size(1024 * 1024)
						.maxDocuments(10)));
		StepVerifier.create(recreateUserAccountCollection).expectNextCount(1).verifyComplete();
			
		Flux<UserAccount> insertUserAccounts = operations.insertAll(Flux.just(user, admin, superadmin).collectList());
		StepVerifier.create(insertUserAccounts).expectNextCount(3).verifyComplete();
    }
}
