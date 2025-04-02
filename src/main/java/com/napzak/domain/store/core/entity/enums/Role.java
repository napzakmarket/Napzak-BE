package com.napzak.domain.store.core.entity.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	STORE("ROLE_STORE"),
	ADMIN("ROLE_ADMIN"),
	ONBOARDING("ROLE_ONBOARDING"),
	REPORTED("ROLE_REPORTED"),
	BLOCKED("ROLE_BLOCKED"),
	WITHDRAWN("ROLE_WITHDRAWN"),
	;

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
