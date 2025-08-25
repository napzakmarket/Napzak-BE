package com.napzak.chat.domain.push.api.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	PUSH_SETTING_RETRIEVE_SUCCESS(HttpStatus.OK, "알림 설정이 조회되었습니다."),
	PUSH_TOKEN_DELETE_SUCCESS(HttpStatus.OK, "푸시 토큰이 삭제되었습니다."),
	PUSH_SETTING_UPDATE_SUCCESS(HttpStatus.OK, "알림 설정이 변경되었습니다."),
	PUSH_TEST_SUCCESS(HttpStatus.OK, "푸시 테스트 전송이 성공하였습니다."),

	/*
	201 Created
	 */
	PUSH_TOKEN_CREATE_SUCCESS(HttpStatus.CREATED, "푸시 토큰이 등록되었습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
