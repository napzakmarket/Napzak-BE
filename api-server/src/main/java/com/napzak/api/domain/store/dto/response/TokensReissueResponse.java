package com.napzak.api.domain.store.dto.response;

import com.napzak.common.auth.role.enums.Role;

public record TokensReissueResponse(
	String accessToken,
	String refreshToken,
	Role role
) {
	public static TokensReissueResponse of(
		final String accessToken,
		final String refreshToken,
		final Role role
	) {
		return new TokensReissueResponse(accessToken, refreshToken, role);
	}
}