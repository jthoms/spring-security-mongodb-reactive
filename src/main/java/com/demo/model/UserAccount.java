package com.demo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Email;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

/**
 * Mongodb {@link UserAccount} Document class.
 *
 * @author John Thoms
 */
@Document
@Data
@Builder
public class UserAccount implements UserDetails {
    @Id 
    private ObjectId id;
       
	@Indexed(unique=true, direction=IndexDirection.DESCENDING, dropDups=true)
    private String username;
	
    private String password;
    
    @Email
    private String email;

    @Builder.Default()
    private boolean enabled = true;
    
    @Builder.Default()
	@DBRef
	private List<Role> roles = new ArrayList<Role>();
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return enabled;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
