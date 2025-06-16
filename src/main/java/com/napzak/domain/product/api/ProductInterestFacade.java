package com.napzak.domain.product.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.napzak.domain.interest.core.InterestRemover;
import com.napzak.domain.interest.core.InterestRetriever;
import com.napzak.domain.interest.core.vo.Interest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductInterestFacade {
	private final InterestRetriever interestRetriever;
	private final InterestRemover interestRemover;

	public boolean getIsInterested(Long productId, Long storeId) {
		return interestRetriever.getIsInterested(productId, storeId);
	}

	public Map<Long, Boolean> getIsInterestedMap(List<Long> productIds, Long storeId) {
		return interestRetriever.areProductsInterested(productIds, storeId);
	}

	public Interest findInterestByProductIdAndStoreId(Long productId, Long storeId) {
		return interestRetriever.retrieveInterestByProductIdAndStoreId(productId, storeId);
	}

	public void deleteAllByProductId(Long productId) {
		interestRemover.deleteAllInterest(productId);
	}

	public List<Interest> findInterestsByStoreId(Long storeId) {
		return interestRetriever.retrieveInterestsByStoreId(storeId);
	}

}
