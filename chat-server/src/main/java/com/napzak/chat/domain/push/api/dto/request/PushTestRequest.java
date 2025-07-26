package com.napzak.chat.domain.push.api.dto.request;

import java.util.Map;

import com.napzak.domain.chat.entity.enums.MessageType;

import jakarta.validation.constraints.NotNull;

public record PushTestRequest(
	@NotNull Long opponentId,
	@NotNull MessageType messageType,
	@NotNull String token,
	@NotNull Notification notification,
	@NotNull Data data
) {
	public record Notification(
		@NotNull String title,
		@NotNull String body
	) {}

	public record Data(
		@NotNull String type,
		@NotNull String roomId
	) {}
}
