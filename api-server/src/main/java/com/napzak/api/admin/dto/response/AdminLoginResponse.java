package com.napzak.api.admin.dto.response;

public record AdminLoginResponse(
	String accessToken,
	String refreshToken
) {
	public static AdminLoginResponse of(
		final String accessToken,
		final String refreshToken
	) {
		return new AdminLoginResponse(accessToken, refreshToken);
	}
}