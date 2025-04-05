package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductSaver {
	private final ProductRepository productRepository;

	public Product save(
		final String title,
		final String description,
		final TradeType tradeType,
		final TradeStatus tradeStatus,
		final int price,
		final Boolean isPriceNegotiable,
		final Boolean isDeliveryIncluded,
		final int standardDeliveryFee,
		final int halfDeliveryFee,
		final ProductCondition productCondition,
		final Long storeId,
		final Long genreId
	) {
		final ProductEntity productEntity = productRepository.save(
			ProductEntity.create(
				title, description, tradeType, tradeStatus, price,
				isPriceNegotiable, isDeliveryIncluded, standardDeliveryFee,
				halfDeliveryFee, productCondition, storeId, genreId
			)
		);

		return Product.fromEntity(productEntity);
	}
}
