package com.napzak.chat.domain.chat.api.dto.response;

public record ChatRoomCreateResponse(
	Long roomId
) {
	public static ChatRoomCreateResponse of(Long roomId) {
		return new ChatRoomCreateResponse(roomId);
	}
}
