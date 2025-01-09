package com.napzak.global.common.exception;

import com.napzak.global.common.exception.base.BaseErrorCode;

public class ConflictException extends NapzakException {
    public ConflictException(final BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
