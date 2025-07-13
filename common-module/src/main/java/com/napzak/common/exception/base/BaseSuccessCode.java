package com.napzak.common.exception.base;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {
	HttpStatus getHttpStatus();

	String getMessage();
}
