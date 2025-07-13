package com.napzak.domain.interest.crud;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.interest.entity.InterestEntity;
import com.napzak.domain.interest.repository.InterestRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InterestSaver {

	private final InterestRepository interestRepository;

	@Transactional
	public void saveInterest(final Long productId, final Long storeId) {

		final InterestEntity interestEntity = InterestEntity.create(productId, storeId);
		interestRepository.save(interestEntity);
	}

}
