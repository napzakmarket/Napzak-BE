package com.napzak.api.domain.store;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.interest.crud.InterestRemover;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreInterestFacade {

	private final InterestRemover interestRemover;

	@Transactional
	public void deleteInterestByStoreId(Long storeId) {
		interestRemover.deleteAllInterestByStoreId(storeId);
	}
}
