package com.napzak.domain.genre.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenreSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	GENRE_LIST_RETRIEVE_SUCCESS(HttpStatus.OK, "장르 목록이 조회되었습니다."),
	GENRE_LIST_SEARCH_SUCCESS(HttpStatus.OK, "검색된 장르 목록이 조회되었습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
