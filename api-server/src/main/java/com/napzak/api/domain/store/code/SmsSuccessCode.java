package com.napzak.api.domain.store.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	VERIFICATION_CODE_SEND_SUCCESS(HttpStatus.OK, "인증번호 발송에 성공하였습니다."),
	VERIFICATION_CODE_CONFIRM_SUCCESS(HttpStatus.OK, "인증번호 검증 성공 여부가 조회되었습니다.")
	;

	private final HttpStatus httpStatus;
	private final String message;

}
