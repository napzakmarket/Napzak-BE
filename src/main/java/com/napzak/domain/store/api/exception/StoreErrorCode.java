package com.napzak.domain.store.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
	/*
	400 Bad Request
 	*/
	INVALID_GENRE_PREFERENCE_COUNT(HttpStatus.BAD_REQUEST, "선호 장르 리스트는 최대 4개까지 입력할 수 있습니다."),
	DUPLICATE_GENRE_PREFERENCES(HttpStatus.BAD_REQUEST, "선호 장르 리스트에 중복된 값이 있습니다."),

	/*
	404 Not Found
	 */
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}
