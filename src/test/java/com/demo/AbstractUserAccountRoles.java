package com.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.model.Role;
import com.demo.model.UserAccount;

import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * Base class for establishing {@link UserAccount} and {@link Role} sample data.
 *
 * @author John Thoms
 */
public class AbstractUserAccountRoles {	
    @Autowired protected PasswordEncoder passwordEncoder;
	
	protected final String USER_USERNAME = "user";
	protected final String ADMIN_USERNAME = "admin";
	protected final String SUPERADMIN_USERNAME = "superadmin";
	
	protected final String USER_PASSWORD = "userpass";
	protected String userPasswordEncoded;

	protected final String ADMIN_PASSWORD = "adminpass";
	protected String adminPasswordEncoded;
	
	protected final String SUPERADMIN_PASSWORD = "superadminpass";
	protected String superadminPasswordEncoded;
	
	protected final String USER_ROLE = "USER";
	protected final String ADMIN_ROLE = "ADMIN";
	protected final String SUPERADMIN_ROLE = "SUPERADMIN";

	protected final Role roleUser = new Role(USER_ROLE);
	protected final Role roleAdmin = new Role(ADMIN_ROLE);
	protected final Role roleSuperAdmin = new Role(SUPERADMIN_ROLE);
	
	protected UserAccount user;
	protected UserAccount admin;
	protected UserAccount superadmin;

	protected final Logger logger = Loggers.getLogger(this.getClass());
	
	@PostConstruct
	void init() {
		userPasswordEncoded = passwordEncoder.encode(USER_PASSWORD);
		adminPasswordEncoded = passwordEncoder.encode(ADMIN_PASSWORD);
		superadminPasswordEncoded = passwordEncoder.encode(SUPERADMIN_PASSWORD);
		
		user = UserAccount.builder().username(USER_USERNAME).password(userPasswordEncoded).email("user@example.com").build();
		user.addRole(roleUser);
		
		admin = UserAccount.builder().username(ADMIN_USERNAME).password(adminPasswordEncoded).email("admin@example.com").build();
		admin.addRole(roleUser);
		admin.addRole(roleAdmin);
		
		superadmin = UserAccount.builder().username(SUPERADMIN_USERNAME).password(superadminPasswordEncoded).email("superadmin@example.com").build();
		superadmin.addRole(roleUser);
		superadmin.addRole(roleAdmin);
		superadmin.addRole(roleSuperAdmin);
	}
}
