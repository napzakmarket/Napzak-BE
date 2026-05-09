package com.napzak.api.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SmsConfirmRequest(
	@NotBlank
	String phoneNumber,

	@NotBlank
	String code
) {
}
