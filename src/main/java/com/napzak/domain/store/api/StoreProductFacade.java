package com.napzak.domain.store.api;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.domain.product.core.ProductUpdater;
import com.napzak.domain.product.core.entity.enums.TradeType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreProductFacade {

	private final ProductRetriever productRetriever;
	private final ProductUpdater productUpdater;

	public int getProductCount(Long storeId, TradeType tradeType) {
		return productRetriever.countProductsByStoreIdAndTradeType(storeId, tradeType);
	}

	@Transactional
	public void updateProductIsVisibleByStoreId(Long storeId) {
		productUpdater.updateProductIsVisibleByStoreId(storeId);
	}
}
