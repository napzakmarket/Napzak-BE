package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.vo.Product;
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

	@Transactional
	public void updateProductStatus(Long productId, TradeStatus tradeStatus) {
		ProductEntity productEntity = productRepository.lockById(productId)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));

		productRepository.updateTradeStatus(productId, tradeStatus);
	}

	@Transactional
	public Product updateProduct(Long productId, String title, String description, int price, Boolean isPriceNegotiable,
		Boolean isDeliveryIncluded, int standardDeliveryFee, int halfDeliveryFee, ProductCondition productCondition, Long genreId) {
		ProductEntity productEntity = productRepository.findById(productId)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));

		productEntity.update(title, description, price, isPriceNegotiable, isDeliveryIncluded, standardDeliveryFee,
			halfDeliveryFee, productCondition, genreId);

		return Product.fromEntity(productEntity);
	}

	@Transactional
	public void updateProductIsVisibleByStoreId(Long storeId) {
		productRepository.updateIsVisible(storeId);
	}
}
