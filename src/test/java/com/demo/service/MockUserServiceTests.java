package com.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.AbstractUserAccountRoles;

import reactor.test.StepVerifier;

/**
 * Tests method level security in {@link UserAccountService} using {@link WithMockUser}.
 *
 * @author John Thoms
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MockUserServiceTests extends AbstractUserAccountRoles {
	@Autowired private UserAccountService userAccountService;
	
	@Test
	public void serviceMethodWithoutAuthentication() {
		StepVerifier.create(userAccountService.helloAdmin()).expectError(AccessDeniedException.class).verify();
	}
	
	@Test
	@WithMockUser(username="user",roles={USER_ROLE})
	public void serviceMethodWithUserAuthentication() {
		StepVerifier.create(userAccountService.helloAdmin()).expectError(AccessDeniedException.class).verify();
	}
	
	@Test
	@WithMockUser(username="admin",roles={USER_ROLE, ADMIN_ROLE})
	public void serviceMethodWithAdminAuthentication() {
		StepVerifier.create(userAccountService.helloAdmin()).expectNext("Hello Admin").verifyComplete();
	}
	
	@Test
	@WithMockUser(username="superadmin",roles={USER_ROLE, ADMIN_ROLE, SUPERADMIN_ROLE})
	public void serviceMethodWithSuperadminAuthentication() {
		StepVerifier.create(userAccountService.helloAdmin()).expectNext("Hello Admin").verifyComplete();
	}
}
