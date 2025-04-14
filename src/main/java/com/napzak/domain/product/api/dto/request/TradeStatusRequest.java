package com.napzak.domain.product.api.dto.request;

import com.napzak.domain.product.core.entity.enums.TradeStatus;

import jakarta.validation.constraints.NotNull;

public record TradeStatusRequest(
	@NotNull
	TradeStatus tradeStatus
) {
}
