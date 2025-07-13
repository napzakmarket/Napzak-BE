package com.napzak.api.domain.store.service;

import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.common.auth.role.RoleProvider;
import com.napzak.common.auth.role.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreRoleProvider implements RoleProvider {

	private final StoreRetriever storeRetriever;

	@Override
	public boolean supports(String authenticationType) {
		return authenticationType.equals("MemberAuthentication")
			|| authenticationType.equals("AdminAuthentication");
	}

	@Override
	public Role provideRole(Long id) {
		return storeRetriever.findRoleByStoreId(id);
	}
}