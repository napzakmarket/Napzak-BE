package com.napzak.api.admin.dto.response;

public record AdminChatRow(
	Long id,
	Long senderId,
	String senderProfile,
	String senderNickname,
	String content,
	String createdAt
) {
}