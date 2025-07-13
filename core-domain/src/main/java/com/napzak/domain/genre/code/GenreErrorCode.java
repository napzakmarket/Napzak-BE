package com.napzak.domain.genre.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenreErrorCode implements BaseErrorCode {
	/*
	404 Not Found
	 */
	GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "장르를 찾을 수 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
