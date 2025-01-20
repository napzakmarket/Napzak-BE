package com.napzak.domain.product.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.api.dto.StoreStatusDto;
import com.napzak.domain.store.core.StoreRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductStoreFacade {

	private final StoreRetriever storeRetriever;

	public StoreStatusDto findStoreStatusDtoByStoreId(Long storeId) {

		return storeRetriever.getStoreStatusDtoById(storeId);
	}
}
