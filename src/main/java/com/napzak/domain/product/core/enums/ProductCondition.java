package com.napzak.domain.product.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCondition {
    NEW("미개봉"),
    LIKE_NEW("아주 좋은 상태"),
    SLIGHTLY_USED("약간의 사용감"),
    USED("사용감 있음")
    ;

    private final String condition;

}
