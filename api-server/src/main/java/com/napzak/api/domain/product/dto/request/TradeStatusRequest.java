package com.napzak.api.domain.product.dto.request;

import com.napzak.domain.product.entity.enums.TradeStatus;

import jakarta.validation.constraints.NotNull;

public record TradeStatusRequest(
	@NotNull
	TradeStatus tradeStatus
) {
}
