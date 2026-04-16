package com.napzak.domain.store.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsErrorCode implements BaseErrorCode {

	/*
	400 BAD REQUEST
	 */
	VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다. 다시 확인해주세요."),

	/*
	429 TOO MANY REQUESTS
	 */
	DAILY_SEND_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "오늘 인증번호 요청 가능 횟수를 초과했습니다."),
	VERIFICATION_FAIL_COUNT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "인증번호를 여러 번 잘못 입력하셨습니다. 인증번호를 재요청해주세요."),

	/*
	500 INTERNAL SERVER ERROR
	 */
	MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증번호 발송에 실패했습니다. 다시 시도해주세요."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
