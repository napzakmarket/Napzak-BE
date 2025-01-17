package com.napzak.domain.product.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.napzak.domain.interest.core.InterestRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductInterestFacade {
	private final InterestRetriever interestRetriever;

	public boolean getIsInterested(Long productId, Long storeId) {
		return interestRetriever.getIsInterested(productId, storeId);
	}

	public Map<Long, Boolean> getIsInterestedMap(List<Long> productIds, Long storeId) {
		return interestRetriever.areProductsInterested(productIds, storeId);
	}
}
