package com.napzak.domain.store.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}
