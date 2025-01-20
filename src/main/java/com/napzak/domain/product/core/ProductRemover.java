package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRemover {

	private final ProductRepository productRepository;

	@Transactional
	public void decrementInterestCount(Long productId) {
		productRepository.decrementInterestCount(productId);
	}
}
