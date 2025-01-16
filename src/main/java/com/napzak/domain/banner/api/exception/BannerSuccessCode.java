package com.napzak.domain.banner.api.exception;

import com.napzak.global.common.exception.base.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BannerSuccessCode implements BaseSuccessCode {

    BANNER_GET_SUCCESS(HttpStatus.OK, "배너 정보 불러오기 성공")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
