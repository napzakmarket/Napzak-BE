package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductUpdater {

	private final ProductRepository productRepository;

	@Transactional
	public void incrementInterestCount(Long productId) {
		ProductEntity productEntity = productRepository.lockById(productId)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		productRepository.incrementInterestCount(productId);
	}

	@Transactional
	public void decrementInterestCount(Long productId) {
		ProductEntity productEntity = productRepository.lockById(productId)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		productRepository.decrementInterestCount(productId);
	}
}
