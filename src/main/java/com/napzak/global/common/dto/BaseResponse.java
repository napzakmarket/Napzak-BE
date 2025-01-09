package com.napzak.global.common.dto;

import org.springframework.http.HttpStatus;

public interface BaseResponse {
    HttpStatus status();
    String message();
}
