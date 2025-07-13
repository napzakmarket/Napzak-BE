package com.napzak.api.domain.store.dto.response;

import com.napzak.common.auth.role.enums.Role;

public record StoreLoginResponse(
	String accessToken,
	String nickname,
	Role role
) {
	public static StoreLoginResponse of(
		final String accessToken,
		String nickname,
		final Role role
	) {
		return new StoreLoginResponse(accessToken, nickname, role);
	}
}
