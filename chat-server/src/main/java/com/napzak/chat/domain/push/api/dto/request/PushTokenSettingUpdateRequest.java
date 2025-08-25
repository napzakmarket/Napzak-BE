package com.napzak.chat.domain.push.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record PushTokenSettingUpdateRequest(
	@NotNull boolean allowMessage
) {
}
