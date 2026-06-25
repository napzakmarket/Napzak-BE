package com.napzak.api.admin.dto.response;

public record AdminLoginResponse(
	String accessToken
) {
	public static AdminLoginResponse of(final String accessToken) {
		return new AdminLoginResponse(accessToken);
	}
}
