package com.napzak.domain.push.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushErrorCode implements BaseErrorCode {
	/*
	400 Bad Request
	*/
	TYPE_NOT_SERVICED(HttpStatus.BAD_REQUEST, "지원하지 않는 content 타입입니다."),

	/*
	404 Not Found
	 */
	PUSH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),

	/*
	500 Internal Server Error
	 */
	PUSH_DELIVERY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "푸시알림 전송에 실패하였습니다."),
	PUSH_UNDELIVERED(HttpStatus.INTERNAL_SERVER_ERROR, "푸시알림 전송에 실패하여 재전송합니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
