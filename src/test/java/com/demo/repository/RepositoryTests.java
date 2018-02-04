package com.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.AbstractPopulatingMongoOperations;

import reactor.test.StepVerifier;

/**
 * Tests {@link UserAccountRepository} and {@link RoleRepository} using sample data.
 *
 * @author John Thoms
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RepositoryTests extends AbstractPopulatingMongoOperations {
	@Autowired private UserAccountRepository userAccountRepository;
	@Autowired private RoleRepository roleRepository;
	
	@Test
	public void verifyPopulatedRepositories() {
		StepVerifier.create(roleRepository.findAll().log()).expectNextCount(3).verifyComplete();
		StepVerifier.create(userAccountRepository.findAll().log()).expectNextCount(3).verifyComplete();
		
		// user
		UserDetails u = userAccountRepository.findByUsername(USER_USERNAME).block();
		assertThat(u).isNotNull();
		assertThat(u.getUsername()).isEqualTo(USER_USERNAME);
		assertThat(u.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser);

		// admin
		u = userAccountRepository.findByUsername(ADMIN_USERNAME).block();
		assertThat(u).isNotNull();
		assertThat(u.getUsername()).isEqualTo(ADMIN_USERNAME);
		assertThat(u.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser, roleAdmin);

		// superadmin
		u = userAccountRepository.findByUsername(SUPERADMIN_USERNAME).block();
		assertThat(u).isNotNull();
		assertThat(u.getUsername()).isEqualTo(SUPERADMIN_USERNAME);
		assertThat(u.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser, roleAdmin, roleSuperAdmin);
	}
}
