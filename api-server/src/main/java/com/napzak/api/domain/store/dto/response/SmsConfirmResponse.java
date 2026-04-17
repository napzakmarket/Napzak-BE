package com.napzak.api.domain.store.dto.response;

public record SmsConfirmResponse (
	boolean isPhoneVerified,
	int remainingRequestCount
) {}
