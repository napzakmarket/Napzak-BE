package com.napzak.domain.store.api.service;

import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.global.auth.role.RoleProvider;
import com.napzak.global.common.exception.NapzakException;

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
		return storeRetriever.findRoleByStoreId(id)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
	}
}