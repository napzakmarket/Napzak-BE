package com.napjak.global.common.exception;

import com.napjak.global.common.exception.base.BaseErrorCode;

import lombok.Getter;

@Getter
public class NapjakException extends RuntimeException {
	private final BaseErrorCode baseErrorCode;

	public NapjakException(BaseErrorCode baseErrorCode) {
		super(baseErrorCode.getMessage());
		this.baseErrorCode = baseErrorCode;
	}
}
