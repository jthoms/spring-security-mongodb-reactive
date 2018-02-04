package com.demo.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.AbstractPopulatingMongoTemplate;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests {@link ReactiveAuthenticationManager} using sample data.
 *
 * @author John Thoms
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthenticationManagerTests extends AbstractPopulatingMongoTemplate {	
	@Autowired private ReactiveAuthenticationManager manager;
	
	@Test
	public void userNotFoundBadCredentials() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(USER_USERNAME + "INVALID", USER_PASSWORD);
		Mono<Authentication> authentication = manager.authenticate(token);

		StepVerifier.create(authentication).expectError(BadCredentialsException.class).verify();
	}
	
	@Test
	public void passwordNotMatchBadCredentials() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(USER_USERNAME, USER_PASSWORD + "INVALID");
		Mono<Authentication> authentication = manager.authenticate(token);

		StepVerifier.create(authentication).expectError(BadCredentialsException.class).verify();
	}
	
	@Test
	public void authenticationSuccess() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(USER_USERNAME, USER_PASSWORD);
		Authentication authentication = manager.authenticate(token).block();
		assertThat(authentication.getName()).isEqualTo(USER_USERNAME);
		assertThat(authentication.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser);
		
		token = new UsernamePasswordAuthenticationToken(ADMIN_USERNAME, ADMIN_PASSWORD);
		authentication = manager.authenticate(token).block();
		assertThat(authentication.getName()).isEqualTo(ADMIN_USERNAME);
		assertThat(authentication.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser, roleAdmin);
		
		token = new UsernamePasswordAuthenticationToken(SUPERADMIN_USERNAME, SUPERADMIN_PASSWORD);
		authentication = manager.authenticate(token).block();
		assertThat(authentication.getName()).isEqualTo(SUPERADMIN_USERNAME);
		assertThat(authentication.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser, roleAdmin, roleSuperAdmin);
	}
}
