package com.napzak.common.auth.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AdminAuthentication extends UsernamePasswordAuthenticationToken {

	public AdminAuthentication(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public Long getAdminId() {
		return (Long)getPrincipal();
	}
}
