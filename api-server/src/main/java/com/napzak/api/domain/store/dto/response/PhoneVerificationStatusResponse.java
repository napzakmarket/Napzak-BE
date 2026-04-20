package com.napzak.api.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhoneVerificationStatusResponse(
	@JsonProperty("isPhoneVerified")
	boolean isPhoneVerified
) {

	public static PhoneVerificationStatusResponse of(final boolean isPhoneVerified) {
		return new PhoneVerificationStatusResponse(isPhoneVerified);
	}
}