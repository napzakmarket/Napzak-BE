package com.napzak.api.domain.store.dto.response;

public record SmsConfirmResponse(
	boolean isCodeMatched,
	int remainingRequestCount
) {}
