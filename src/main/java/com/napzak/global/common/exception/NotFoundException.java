package com.napzak.global.common.exception;

import com.napzak.global.common.exception.base.BaseErrorCode;

public class NotFoundException extends NapzakException{
    public NotFoundException(final BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
