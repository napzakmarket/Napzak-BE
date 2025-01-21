package com.napzak.domain.interest.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.core.ProductUpdater;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InterestProductFacade {

	private final ProductUpdater productUpdater;

	public void incrementInterestCount(Long productId) {
		productUpdater.incrementInterestCount(productId);
	}

	public void decrementInterestCount(Long productId) {
		productUpdater.decrementInterestCount(productId);
	}

}
