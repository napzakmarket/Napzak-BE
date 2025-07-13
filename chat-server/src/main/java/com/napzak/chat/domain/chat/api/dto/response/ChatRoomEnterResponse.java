package com.napzak.chat.domain.chat.api.dto.response;

public record ChatRoomEnterResponse(
	Long productId
) {
	public static ChatRoomEnterResponse of(Long productId) {
		return new ChatRoomEnterResponse(productId);
	}
}
