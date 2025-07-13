package com.napzak.chat.domain.chat.api.dto.request;

public record ChatRoomProductIdUpdateRequest(
	Long newProductId
) {
	public static ChatRoomProductIdUpdateRequest of(Long newProductId) {
		return new ChatRoomProductIdUpdateRequest(newProductId);
	}
}
