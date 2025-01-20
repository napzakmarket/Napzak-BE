package com.napzak.domain.product.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements BaseErrorCode {
	/*
	400 Bad Request
	*/
	INVALID_CURSOR_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 커서입니다."),
	PRODUCT_PHOTO_SEQUENCE_DUPLICATED(HttpStatus.BAD_REQUEST, "사진 순서는 중복될 수 없습니다."),

	/*
	404 Not Found
	 */
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	PRODUCT_PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 사진을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
