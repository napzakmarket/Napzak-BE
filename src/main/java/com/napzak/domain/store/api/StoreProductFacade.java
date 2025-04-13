package com.napzak.domain.store.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.domain.product.core.entity.enums.TradeType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreProductFacade {

	private final ProductRetriever productRetriever;

	public int getProductCount(Long storeId, TradeType tradeType) {
		return productRetriever.countProductsByStoreIdAndTradeType(storeId, tradeType);
	}
}
