package com.napzak.global.auth.role;

import com.napzak.domain.store.core.entity.enums.Role;

public interface RoleProvider {
	boolean supports(String authenticationType);
	Role provideRole(Long id);
}
