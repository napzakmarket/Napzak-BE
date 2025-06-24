package com.napzak.global.external.s3.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements BaseErrorCode {
	/*
	403 FORBIDDEN
	 */
	S3_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S3 접근이 거부되었습니다."),

	/*
	5000
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 오류가 발생했습니다."),

	/*
	503
	 */
	S3_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "S3 서비스에 접근할 수 없습니다.")
	;

	private final HttpStatus httpStatus;
	private final String message;

}
