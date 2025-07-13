package com.napzak.common.auth.role;

import com.napzak.common.auth.role.enums.Role;

public interface RoleProvider {
	boolean supports(String authenticationType);
	Role provideRole(Long id);
}
