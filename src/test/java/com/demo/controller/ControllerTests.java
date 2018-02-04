package com.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.demo.AbstractPopulatingUserAccountService;
import com.demo.security.SecurityConfig;
/**
 * Tests {@link MainController} using sample data; see {@link SecurityConfig.securityWebFilterChain} for pathMatchers, etc.
 *
 * @author John Thoms
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ControllerTests extends AbstractPopulatingUserAccountService {
	@Autowired ApplicationContext context;
	
	private WebTestClient client;

	@BeforeEach
	public void setup() {
		this.client = WebTestClient
				.bindToApplicationContext(this.context)
				.configureClient()
				.build();
	}
	
	@Test
	public void helloPublicByNotAuthenticated() {
		this.client
			.get()
			.uri("/public/hello")
			.exchange()
			.expectStatus().isOk();
	}
	
	@Test
	public void helloAuthenticatedByNotAuthenticated() {
		this.client
			.get()
			.uri("/hello")
			.exchange()
			.expectStatus().isUnauthorized();
	}
	
	@Test
	@WithMockUser(username="user",roles={USER_ROLE})
	public void helloAuthenticatedByAuthenticatedUser() {
		this.client
			.get()
			.uri("/hello")
			.exchange()
			.expectStatus().isOk();
	}
	
	@Test
	@WithMockUser(username="user",roles={USER_ROLE})
	public void helloAdminByAuthenticatedUser() {
		this.client
			.get()
			.uri("/helloadmin")
			.exchange()
			.expectStatus().isForbidden();
	}
	
	@Test
	@WithMockUser(username="admin",roles={USER_ROLE, ADMIN_ROLE})
	public void helloAdminByAuthenticatedAdmin() {
		this.client
			.get()
			.uri("/helloadmin")
			.exchange()
			.expectStatus().isOk();
	}
	
	@Test
	@WithMockUser(username="superadmin",roles={USER_ROLE, ADMIN_ROLE, SUPERADMIN_ROLE})
	public void helloAdminByAuthenticatedSuperadmin() {
		this.client
			.get()
			.uri("/helloadmin")
			.exchange()
			.expectStatus().isOk();
	}
	
	@Test
	@WithMockUser(username="user",roles={USER_ROLE})
	public void helloAdmin2ByAuthenticatedUser() {
		this.client
			.get()
			.uri("/helloadmin")
			.exchange()
			.expectStatus().isForbidden();
	}
	
	@Test
	@WithMockUser(username="admin",roles={USER_ROLE, ADMIN_ROLE})
	public void helloAdmin2ByAuthenticatedAdmin() {
		this.client
			.get()
			.uri("/helloadmin2")
			.exchange()
			.expectStatus().isOk();
	}
	
	@Test
	@WithMockUser(username="admin",roles={USER_ROLE, ADMIN_ROLE})
	public void helloSuperadminByAuthenticatedAdmin() {
		this.client
			.get()
			.uri("/hellosuperadmin")
			.exchange()
			.expectStatus().isForbidden();
	}
	
	@Test
	@WithMockUser(username="superadmin",roles={USER_ROLE, ADMIN_ROLE, SUPERADMIN_ROLE})
	public void helloSuperadminByAuthenticatedSuperadmin() {
		this.client
			.get()
			.uri("/hellosuperadmin")
			.exchange()
			.expectStatus().isOk();
	}
}
