package com.napzak.api.domain.store.dto.request;

import jakarta.validation.constraints.Pattern;

public record SmsConfirmRequest(
	@Pattern(regexp = "^01[016789]\\d{7,8}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
	String phoneNumber,

	@Pattern(regexp = "^\\d{6}$", message = "올바른 인증 번호 형식이 아닙니다.")
	String code
) {
}
