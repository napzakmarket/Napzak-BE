package com.napzak.domain.product.api.exception;

import com.napzak.global.common.exception.base.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements BaseSuccessCode {
    PRODUCT_GET_SUCCESS(HttpStatus.OK, "상품 정보 불러오기 성공"),
    BANNER_GET_SUCCESS(HttpStatus.OK, "배너 정보 불러오기 성공")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
