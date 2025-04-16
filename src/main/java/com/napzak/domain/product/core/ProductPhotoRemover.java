package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPhotoRemover {

	private final ProductPhotoRepository productPhotoRepository;

	@Transactional
	public void deleteAllByProductId(Long productId){
		productPhotoRepository.deleteByProductId(productId);
	}
}
