package com.napzak.domain.product.core.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeStatus {

    BEFORE_TRADE("거래중"),
    RESERVED("예약중"),
    COMPLETED("판매완료")
    ;

    private final String trade_status;
}
