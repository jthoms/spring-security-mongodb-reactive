package com.demo.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.demo.model.UserAccount;
import com.demo.repository.UserAccountRepository;

import reactor.core.publisher.Mono;

/**
 * Exposes {@link UserAccountRepository} at the service level.
 *
 * @author John Thoms
 */
@Service
public class UserAccountService {
	@Autowired private UserAccountRepository userAccountRepository;
	
	public Mono<Long> count() {
		return userAccountRepository.count();
	}
	
	public Mono<UserAccount> findByUsername(ObjectId id) {
		return userAccountRepository.findById(id);
	}
	
	public Mono<UserDetails> findByUsername(String username) {
		return userAccountRepository.findByUsername(username);
	}
	
	public Mono<UserAccount> findUserAccountByUsername(String username) {
		return userAccountRepository.findUserAccountByUsername(username);
	}
	
	public Mono<UserAccount> create(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
	}
	
	public Mono<UserAccount> update(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
	}

	public Mono<Void> delete(UserAccount userAccount) {
		return userAccountRepository.delete(userAccount);
	}
	
	public Mono<Void> deleteAll() {
		return userAccountRepository.deleteAll();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<String> helloAdmin() {
		return Mono.just("Hello Admin");
	}
}
