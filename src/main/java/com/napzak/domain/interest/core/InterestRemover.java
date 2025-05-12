package com.napzak.domain.interest.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InterestRemover {

	private final InterestRepository interestRepository;

	@Transactional
	public void deleteInterest(Long productId, Long storeId) {

		interestRepository.deleteByProductIdAndStoreId(productId, storeId);

	}

	@Transactional
	public void deleteAllInterest(Long productId) {
		interestRepository.deleteByProductId(productId);
	}
}
