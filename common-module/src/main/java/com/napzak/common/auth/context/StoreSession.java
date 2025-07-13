package com.napzak.common.auth.context;

import com.napzak.common.auth.role.enums.Role;

import lombok.Getter;

@Getter
public class StoreSession {
	private final Long id;
	private final Role role;

	public StoreSession(Long id, Role role) {
		this.id = id;
		this.role = role;
	}

	public static StoreSession of(Long id, Role role) {
		return new StoreSession(id, role);
	}
}
