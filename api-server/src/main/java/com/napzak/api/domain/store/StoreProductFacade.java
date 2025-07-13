package com.napzak.api.domain.store;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.crud.product.ProductRetriever;
import com.napzak.domain.product.crud.product.ProductUpdater;
import com.napzak.domain.product.entity.enums.TradeType;

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
