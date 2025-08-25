package com.napzak.domain.product.crud.product;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.entity.ProductEntity;
import com.napzak.domain.product.entity.enums.ProductCondition;
import com.napzak.domain.product.entity.enums.TradeStatus;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.repository.ProductRepository;
import com.napzak.domain.product.vo.Product;

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
				halfDeliveryFee, productCondition, storeId, genreId, true
			)
		);

		return Product.fromEntity(productEntity);
	}
}
