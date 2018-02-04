package com.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.demo.repository.UserAccountRepository;

/**
 * Create {@link ReactiveAuthenticationManager} authenticationManager with {@link BCryptPasswordEncoder},
 * and {@link SecurityWebFilterChain}.
 *
 * @author John Thoms
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/superadmin").hasAuthority("SUPERADMIN")
                .pathMatchers("/admin").hasAuthority("ADMIN")
                .pathMatchers("/user").hasAuthority("USER")
                .pathMatchers("/public/**").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() { 	
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ReactiveAuthenticationManager authenticationManager(UserAccountRepository userAccountRepository) {
    	UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(userAccountRepository);
    	manager.setPasswordEncoder(passwordEncoder());		
        return manager;
    }
}