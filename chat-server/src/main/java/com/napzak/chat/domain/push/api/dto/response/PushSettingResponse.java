package com.napzak.chat.domain.push.api.dto.response;

public record PushSettingResponse(
	boolean allowMessage
) {
	public static PushSettingResponse of(boolean allowMessage) {
		return new PushSettingResponse(allowMessage);
	}
}
