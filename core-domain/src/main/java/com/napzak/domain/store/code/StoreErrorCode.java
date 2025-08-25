package com.napzak.domain.store.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

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
	NICKNAME_CONTAINS_SLANG(HttpStatus.BAD_REQUEST, "욕설이나 비속어를 사용할 수 없어요."),
	SOCIAL_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 타입입니다."),

	/*
	403 Forbidden
	 */
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	REPORTED_USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "신고 처리된 계정으로, 로그인이 제한되었습니다."),
	/*

	404 Not Found
	 */
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 없습니다."),

	/*
	409 Conflict
	 */
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 이름이에요."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}
