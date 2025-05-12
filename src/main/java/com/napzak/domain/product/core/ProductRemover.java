package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.api.ProductInterestFacade;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRemover {

	private final ProductRepository productRepository;
	private final ProductPhotoRepository productPhotoRepository;
	private final ProductInterestFacade productInterestFacade;

	public void deleteById(Long productId) {

		productPhotoRepository.deleteAllByProductId(productId);
		productInterestFacade.deleteAllByProductId(productId);
		productRepository.deleteById(productId);


	}
}
