package com.youran.gogoboard.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

	public UserAuthentication(Object principal, Object credentials) {
		super(principal, credentials);
		
	}

}
