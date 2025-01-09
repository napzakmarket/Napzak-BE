package com.napzak.global.common.exception;

import com.napzak.global.common.exception.base.BaseErrorCode;

public class BadRequestException extends NapzakException {
    public BadRequestException(final BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}