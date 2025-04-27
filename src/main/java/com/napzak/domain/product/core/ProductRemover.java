package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRemover {

	private final ProductRepository productRepository;
	private final ProductPhotoRepository productPhotoRepository;

	public void deleteById(Long productId) {

		productPhotoRepository.deleteAllByProductId(productId);
		productRepository.deleteById(productId);

	}
}
