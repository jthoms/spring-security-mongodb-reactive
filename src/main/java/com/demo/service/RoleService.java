package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Role;
import com.demo.repository.RoleRepository;

import reactor.core.publisher.Mono;

/**
 * Exposes {@link RoleRepository} at the service level.
 *
 * @author John Thoms
 */
@Service
public class RoleService {
	@Autowired private RoleRepository roleRepository;
	
	public Mono<Role> create(Role role) {
		return roleRepository.save(role);
	}
	
	public Mono<Void> deleteAll() {
		return roleRepository.deleteAll();
	}
}
