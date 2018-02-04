package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.service.UserAccountService;

import reactor.core.publisher.Mono;

/**
 * RestController sample implementation.
 * 
 * @author John Thoms
 */
@RestController
public class MainController {
	@Autowired private UserAccountService userAccountService;

	@GetMapping("/public/hello")
	public Mono<String> helloPublic() {
		return Mono.just("Hello");
	}
	
	@GetMapping("/hello")
	public Mono<String> helloAuthenticated() {
		return Mono.just("Hello");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/helloadmin")
	public Mono<String> helloAdmin() {
		return Mono.just("Hello");
	}
	
	// constrained by userAccountService method @PreAuthorize
	@GetMapping("/helloadmin2")
	public Mono<String> helloAdmin2() {
		return userAccountService.helloAdmin();
	}
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@GetMapping("/hellosuperadmin")
	public Mono<String> helloSuperadmin() {
		return Mono.just("Hello");
	}

}
