package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRemover {

	private final ProductRepository productRepository;

	public void deleteById(Long productId) {

		productRepository.deleteById(productId);

	}
}
