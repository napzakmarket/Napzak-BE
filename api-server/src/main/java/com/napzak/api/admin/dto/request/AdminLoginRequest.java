package com.napzak.api.admin.dto.request;

public record AdminLoginRequest(
	String loginId,
	String password
) {
}