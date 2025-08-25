package com.napzak.domain.product.crud.product;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.repository.ProductPhotoRepository;
import com.napzak.domain.product.repository.ProductRepository;

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
