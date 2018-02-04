package com.demo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.model.Role;
import com.demo.model.UserAccount;
import com.demo.service.RoleService;
import com.demo.service.UserAccountService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Populates {@link UserAccount} and {@link Role} sample data via {@link RoleService} and {@link UserAccountService}.
 *
 * @author John Thoms
 */
public class AbstractPopulatingUserAccountService extends AbstractUserAccountRoles {
	@Autowired private RoleService roleService;
	@Autowired private UserAccountService userAccountService;
	
    @BeforeEach
    public void setUp() {
		StepVerifier.create(roleService.deleteAll()).verifyComplete();
		StepVerifier.create(userAccountService.deleteAll()).verifyComplete();
		
		Mono<Role> createRoleUser = roleService.create(roleUser);
		StepVerifier.create(createRoleUser).expectNextCount(1).verifyComplete();
		Mono<Role> createRoleAdmin = roleService.create(roleAdmin);
		StepVerifier.create(createRoleAdmin).expectNextCount(1).verifyComplete();
		Mono<Role> createRoleSuperAdmin = roleService.create(roleSuperAdmin);
		StepVerifier.create(createRoleSuperAdmin).expectNextCount(1).verifyComplete();

		Mono<UserAccount> createUser = userAccountService.create(user);
		StepVerifier.create(createUser).expectNextCount(1).verifyComplete();
		Mono<UserAccount> createAdmin = userAccountService.create(admin);
		StepVerifier.create(createAdmin).expectNextCount(1).verifyComplete();
		Mono<UserAccount> createSuperAdmin = userAccountService.create(superadmin);
		StepVerifier.create(createSuperAdmin).expectNextCount(1).verifyComplete();
    }
}
