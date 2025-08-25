package com.napzak.api.admin.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	STORE_REPORT_APPROVE_SUCCESS(HttpStatus.OK, "유저 신고 승인이 완료되었습니다."),
	;
	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}
