package com.napzak.domain.product.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeType {

    SELL("팔아요"),
    BUY("구해요"),
    ;

    private final String trade_type;
}
