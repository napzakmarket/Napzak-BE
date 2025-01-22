package com.napzak.domain.interest.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterestErrorCode implements BaseErrorCode {
	/*
	400 Bad Request
	 */
	INTEREST_ALREADY_POSTED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 상품입니다."),
	INTEREST_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 데이터가 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}
