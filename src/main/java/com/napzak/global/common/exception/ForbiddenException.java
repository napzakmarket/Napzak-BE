package com.napzak.global.common.exception;

import com.napzak.global.common.exception.base.BaseErrorCode;

public class ForbiddenException extends NapzakException{
    public ForbiddenException(final BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
