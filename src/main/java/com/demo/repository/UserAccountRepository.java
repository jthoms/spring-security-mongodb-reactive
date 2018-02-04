package com.demo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import com.demo.model.UserAccount;

import reactor.core.publisher.Mono;

public interface UserAccountRepository extends ReactiveCrudRepository<UserAccount, ObjectId>, ReactiveUserDetailsService {
    Mono<UserDetails> findByUsername(String username);
    Mono<UserAccount> findUserAccountByUsername(String username);
}