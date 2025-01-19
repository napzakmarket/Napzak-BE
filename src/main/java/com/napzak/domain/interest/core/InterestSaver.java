package com.napzak.domain.interest.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.interest.core.entity.InterestEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InterestSaver {

	private final InterestRepository interestRepository;
	private final InterestProductFacade interestProductFacade;

	@Transactional
	public void saveInterest(final Long productId, final Long storeId) {

		final InterestEntity interestEntity = InterestEntity.create(productId, storeId);
		interestRepository.save(interestEntity);
		interestProductFacade.incrementInterestCount(productId);
	}

}
