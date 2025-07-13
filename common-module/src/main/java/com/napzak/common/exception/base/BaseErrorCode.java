package com.napzak.common.exception.base;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
	HttpStatus getHttpStatus();

	String getMessage();
}
