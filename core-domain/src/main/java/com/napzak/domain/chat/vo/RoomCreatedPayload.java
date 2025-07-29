package com.napzak.domain.chat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoomCreatedPayload(
	Long roomId,
	Long storeId,
	int retryCount
) {
	public static RoomCreatedPayload from(Long roomId, Long storeId) {
		return new RoomCreatedPayload(roomId, storeId, 0);
	}

	public static RoomCreatedPayload retry(RoomCreatedPayload payload) {
		return new RoomCreatedPayload(payload.roomId(), payload.storeId(), payload.retryCount() + 1);
	}
}
