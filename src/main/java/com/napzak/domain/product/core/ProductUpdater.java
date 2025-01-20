package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductUpdater {

	private final ProductRepository productRepository;

	@Transactional
	public void incrementInterestCount(Long productId) {
		productRepository.incrementInterestCount(productId);
	}
}
