package com.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.demo.model.Role;

public interface RoleRepository extends ReactiveCrudRepository<Role, String> {
}
