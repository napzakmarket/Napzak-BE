package com.napzak.domain.admin.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements BaseErrorCode {
	/*
	401 Unauthorized
	 */
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

	/*
	404 Not Found
	 */
	ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "어드민 계정을 찾을 수 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
