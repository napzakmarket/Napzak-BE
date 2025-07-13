package com.napzak.chat.domain.push.api.dto.request;

import com.napzak.common.annotation.ValidEnum;
import com.napzak.domain.push.entity.enums.Platform;

import jakarta.validation.constraints.NotNull;

public record PushTokenRegisterRequest(
	@NotNull String deviceToken,
	@ValidEnum(enumClass = Platform.class, message = "유효하지 않은 platform입니다.")
	@NotNull Platform platform,
	@NotNull Boolean isEnabled,
	@NotNull Boolean allowMessage
) {
}
