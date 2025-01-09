package com.napzak.global.common.dto;

import com.napzak.domain.member.exception.MemberSuccessCode;
import org.springframework.http.HttpStatus;

public record SuccessResponse<T>(
        HttpStatus status,
        String message,
        T data
) implements BaseResponse {

    public static <T> SuccessResponse<T> of(MemberSuccessCode code, T data){
        return new SuccessResponse<>(
            code.getHttpStatus(),
            code.getMessage(),
            data
        );
    }
}
