package com.napzak.domain.store.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record StoreWithdrawRequest(
	@NotBlank
	String withdrawTitle,
	String withdrawDescription) {
}
