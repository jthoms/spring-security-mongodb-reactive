package com.demo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.demo.model.Role;
import com.demo.model.UserAccount;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Populates {@link UserAccount} and {@link Role} sample data via {@link ReactiveMongoTemplate}.
 *
 * @author John Thoms
 */
public class AbstractPopulatingMongoTemplate extends AbstractUserAccountRoles {
	@Autowired private ReactiveMongoTemplate template;
	
    @BeforeEach
    public void setUp() {
		StepVerifier.create(template.dropCollection(Role.class)).verifyComplete();
		StepVerifier.create(template.dropCollection(UserAccount.class)).verifyComplete();

		// recreate Roles
		Flux<Role> insertRoles = template.insertAll(Flux.just(roleUser, roleAdmin, roleSuperAdmin).collectList());
		StepVerifier.create(insertRoles).expectNextCount(3).verifyComplete();
		
		// recreate UserAccounts
		Flux<UserAccount> insertUserAccounts = template.insertAll(Flux.just(user, admin, superadmin).collectList());
		StepVerifier.create(insertUserAccounts).expectNextCount(3).verifyComplete();
    }
}
