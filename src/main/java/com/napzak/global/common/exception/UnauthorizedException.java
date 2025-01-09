package com.napzak.global.common.exception;

import com.napzak.global.common.exception.base.BaseErrorCode;

public class UnauthorizedException extends NapzakException{
    public UnauthorizedException(final BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
