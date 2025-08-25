package com.napzak.chat.domain.chat.api.dto.response;

public record ChatRoomProductIdUpdateResponse (
	Long updatedProductId
){
	public static ChatRoomProductIdUpdateResponse of(Long updatedProductId) {
		return new ChatRoomProductIdUpdateResponse(updatedProductId);
	}
}
