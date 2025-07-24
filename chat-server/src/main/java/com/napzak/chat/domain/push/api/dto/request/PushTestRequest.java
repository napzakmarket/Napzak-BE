package com.napzak.chat.domain.push.api.dto.request;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

public record PushTestRequest(
	@NotNull String deviceToken,
	@NotNull String title,
	@NotNull String body,
	Map<String, String> data
) {
}
