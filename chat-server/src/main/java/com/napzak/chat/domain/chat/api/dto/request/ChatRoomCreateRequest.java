package com.napzak.chat.domain.chat.api.dto.request;

public record ChatRoomCreateRequest(
	Long productId,
	Long receiverId
) {
}
