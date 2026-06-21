package com.napzak.domain.admin.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements BaseErrorCode {
	/*
	400 Bad Request
	 */
	STORE_ROLE_REQUIRED(HttpStatus.BAD_REQUEST, "유저의 role이 store가 아니므로 요청에 실패했습니다."),
	INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 ROLE입니다."),

	/*
	401 Unauthorized
	 */
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

	/*
	404 Not Found
	 */
	ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "어드민 계정을 찾을 수 없습니다."),

	/*
	409 Conflict
	 */
	REPORT_PROCESSING_CONFLICT(HttpStatus.CONFLICT, "다른 처리가 진행 중입니다. 잠시 후 다시 시도해주세요."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
