package com.napjak.domain.member.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN");

	private final String roleName;

	/**
	 * GrantedAuthority로 변환하는 메서드.
	 * Spring Security에서 사용자 권한을 처리할 때 사용.
	 */
	public GrantedAuthority toGrantedAuthority() {
		return new SimpleGrantedAuthority(roleName);
	}

	/**
	 * 역할 이름을 반환하는 메서드.
	 * 예: "ROLE_USER", "ROLE_MEMBER", "ROLE_ADMIN".
	 */
	public String getRoleName() {
		return this.roleName;
	}
}
