package com.napzak.common.exception;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;

@Getter
public class NapzakException extends RuntimeException {
	private final BaseErrorCode baseErrorCode;

	public NapzakException(BaseErrorCode baseErrorCode) {
		super(baseErrorCode.getMessage());
		this.baseErrorCode = baseErrorCode;
	}
}
