package com.napzak.api.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SmsSendRequest(
	@NotBlank
	String phoneNumber
) {
}
