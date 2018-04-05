package com.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.AbstractPopulatingUserAccountService;
import com.demo.model.UserAccount;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests {@link UserAccountService} using sample data.
 *
 * @author John Thoms
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ServiceTests extends AbstractPopulatingUserAccountService {
	@Autowired private UserAccountService userAccountService;
	
	private String username1 = USER_USERNAME + "1";

	@Test
	public void countUserAccounts() {
		Mono<Long> count = userAccountService.count();
		StepVerifier.create(count).expectNext(3L).verifyComplete();
	}
	
	@Test
	public void verifyPopulatedUserAccounts() {
		UserDetails userdetails = userAccountService.findByUsername(USER_USERNAME).block();
		assertThat(userdetails).isNotNull();
		assertThat(userdetails.getUsername()).isEqualTo(USER_USERNAME);
		assertThat(userdetails.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser);
		
		UserAccount useraccount = userAccountService.findUserAccountByUsername(userdetails.getUsername()).block();
		assertThat(useraccount).isNotNull();
		assertThat(useraccount.getUsername()).isEqualTo(userdetails.getUsername());
		
		userdetails = userAccountService.findByUsername(ADMIN_USERNAME).block();
		assertThat(userdetails).isNotNull();
		assertThat(userdetails.getUsername()).isEqualTo(ADMIN_USERNAME);
		assertThat(userdetails.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser, roleAdmin);
		
		useraccount = userAccountService.findUserAccountByUsername(userdetails.getUsername()).block();
		assertThat(useraccount).isNotNull();
		assertThat(useraccount.getUsername()).isEqualTo(userdetails.getUsername());
		
		userdetails = userAccountService.findByUsername(SUPERADMIN_USERNAME).block();
		assertThat(userdetails).isNotNull();
		assertThat(userdetails.getUsername()).isEqualTo(SUPERADMIN_USERNAME);
		assertThat(userdetails.getAuthorities().toArray()).containsExactlyInAnyOrder(roleUser, roleAdmin, roleSuperAdmin);
		
		useraccount = userAccountService.findUserAccountByUsername(userdetails.getUsername()).block();
		assertThat(useraccount).isNotNull();
		assertThat(useraccount.getUsername()).isEqualTo(userdetails.getUsername());
	}
	
	@Test
	public void createUserAccount() {
		UserAccount user1 =  UserAccount.builder().username(username1).password(userPasswordEncoded).email("user@example.com").build();
		UserAccount user1created = userAccountService.create(user1).block();
		
		assertThat(user1created.getUsername()).isEqualTo(user1.getUsername());
		assertThat(userAccountService.findUserAccountByUsername(username1).block()).isEqualTo(user1created);
	}
	
	@Test
	public void updateUserAccount() {
		UserAccount user = userAccountService.findUserAccountByUsername(USER_USERNAME).block();
		assertThat(user.getUsername()).isEqualTo(USER_USERNAME);
		assertThat(user.getEmail()).isEqualTo("user@example.com");
		
		user.setEmail("anotheruser@example.com");
		user = userAccountService.update(user).block();	
		assertThat(user.getEmail()).isEqualTo("anotheruser@example.com");
	}
	
	@Test
	public void deleteUserAccount() {
		UserAccount user1 =  UserAccount.builder().username(this.username1).password(userPasswordEncoded).email("user@example.com").build();
		UserAccount user1created = userAccountService.create(user1).block();
		
		assertThat(user1created.getUsername()).isEqualTo(user1.getUsername());
		assertThat(userAccountService.findUserAccountByUsername(username1).block()).isEqualTo(user1created);
		
		userAccountService.delete(user1created).block();
		assertThat(userAccountService.findUserAccountByUsername(username1).block()).isNull();
	}
	
	@Test
	public void duplicateUsernameException() {
	    Throwable exception = assertThrows(RuntimeException.class, () -> {
	    	UserAccount dupUser =  UserAccount.builder().username(USER_USERNAME).password(userPasswordEncoded).email("user@example.com").build();
	    	userAccountService.create(dupUser).block();
	    });
		assertThat(exception.getMessage().equals("User exists"));
	}
}
