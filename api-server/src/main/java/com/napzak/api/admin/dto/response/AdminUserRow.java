package com.napzak.api.admin.dto.response;

public record AdminUserRow(
	Long id,
	String photo,
	String nickname,
	String role,
	String createdAt
) {
}