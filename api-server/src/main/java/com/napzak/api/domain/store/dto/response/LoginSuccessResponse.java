package com.napzak.api.domain.store.dto.response;

import com.napzak.common.auth.role.enums.Role;

public record LoginSuccessResponse(
	String accessToken,
	String refreshToken,
	String nickname,
	Role role
) {
	public static LoginSuccessResponse of(
		final String accessToken,
		final String refreshToken,
		final String nickname,
		final Role role
	) {
		return new LoginSuccessResponse(accessToken, refreshToken, nickname, role);
	}
}