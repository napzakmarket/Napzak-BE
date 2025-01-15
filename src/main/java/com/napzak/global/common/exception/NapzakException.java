package com.napzak.global.common.exception;

import com.napzak.global.common.exception.base.BaseErrorCode;

import lombok.Getter;

@Getter
public class NapzakException extends RuntimeException {
	private final BaseErrorCode baseErrorCode;

	public NapzakException(BaseErrorCode baseErrorCode) {
		super(baseErrorCode.getMessage());
		this.baseErrorCode = baseErrorCode;
	}
}
