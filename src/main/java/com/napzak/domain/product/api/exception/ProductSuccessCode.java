package com.napzak.domain.product.api.exception;

import com.napzak.global.common.exception.base.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements BaseSuccessCode {
    RECOMMEND_PRODUCT_GET_SUCCESS(HttpStatus.OK, "개인화 상품 불러오기 성공"),
    TOP_SELL_PRODUCT_GET_SUCCESS(HttpStatus.OK, "팔아요 인기 상품 불러오기 성공"),
    TOP_BUY_PRODUCT_GET_SUCCESS(HttpStatus.OK, "구해요 인기 상품 불러오기 성공");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
