package com.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Mongodb {@link Role} Document class.
 *
 * @author John Thoms
 */
@Document
@Data
@AllArgsConstructor
public class Role implements GrantedAuthority {	
    @Id private String id;
    
	@Override
	public String getAuthority() {
		return id;
	}
}
