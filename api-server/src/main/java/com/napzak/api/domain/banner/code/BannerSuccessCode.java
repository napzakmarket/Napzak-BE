package com.napzak.api.domain.banner.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BannerSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	BANNER_GET_SUCCESS(HttpStatus.OK, "배너 정보를 불러왔습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

}