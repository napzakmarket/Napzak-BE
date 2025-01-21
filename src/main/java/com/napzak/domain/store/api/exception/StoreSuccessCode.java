package com.napzak.domain.store.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreSuccessCode implements BaseSuccessCode {
	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),

	//상점 정보 조회
	GET_MYPAGE_INFO_SUCCESS(HttpStatus.OK, "마이페이지 정보 조회 성공"),
	GET_STORE_INFO_SUCCESS(HttpStatus.OK, "상점 정보 조회 성공"),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

}
