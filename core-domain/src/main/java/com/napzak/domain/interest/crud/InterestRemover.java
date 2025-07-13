package com.napzak.domain.interest.crud;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.interest.repository.InterestRepository;

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

	@Transactional
	public void deleteAllInterestByStoreId(Long storeId) { interestRepository.deleteByStoreId(storeId);}
}
