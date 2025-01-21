package com.napzak.domain.interest.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterestSuccessCode implements BaseSuccessCode {

	POST_INTEREST_SUCCESS(HttpStatus.NO_CONTENT, "좋아요 설정 요청이 성공했습니다."),
	DELETE_INTEREST_SUCCESS(HttpStatus.NO_CONTENT, "좋아요 해제 요청이 성공했습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

}
