package com.napzak.domain.product.core.vo;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {
	private final Long id;
	private final Long storeId;
	private final String title;
	private final String description;
	private final TradeType tradeType;
	private final TradeStatus tradeStatus;
	private final int price;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final int viewCount;
	private final Boolean isPriceNegotiable;
	private final Boolean isDeliveryIncluded;
	private final int standardDeliveryFee;
	private final int halfDeliveryFee;
	private final ProductCondition productCondition;
	private final int interestCount;
	private final Long genreId;

	public Product(Long id, Long storeId, String title, String description, TradeType tradeType,
		TradeStatus tradeStatus, int price, LocalDateTime createdAt, LocalDateTime updatedAt,
		int viewCount, Boolean isPriceNegotiable, Boolean isDeliveryIncluded,
		int standardDeliveryFee, int halfDeliveryFee, ProductCondition productCondition,
		int interestCount, Long genreId) {
		this.id = id;
		this.storeId = storeId;
		this.title = title;
		this.description = description;
		this.tradeType = tradeType;
		this.tradeStatus = tradeStatus;
		this.price = price;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.viewCount = viewCount;
		this.isPriceNegotiable = isPriceNegotiable;
		this.isDeliveryIncluded = isDeliveryIncluded;
		this.standardDeliveryFee = standardDeliveryFee;
		this.halfDeliveryFee = halfDeliveryFee;
		this.productCondition = productCondition;
		this.interestCount = interestCount;
		this.genreId = genreId;
	}

	public static Product fromEntity(ProductEntity entity) {
		return new Product(
			entity.getId(),
			entity.getStoreId(),
			entity.getTitle(),
			entity.getDescription(),
			entity.getTradeType(),
			entity.getTradeStatus(),
			entity.getPrice(),
			entity.getCreatedAt(),
			entity.getUpdatedAt(),
			entity.getViewCount(),
			entity.getIsPriceNegotiable(),
			entity.getIsDeliveryIncluded(),
			entity.getStandardDeliveryFee(),
			entity.getHalfDeliveryFee(),
			entity.getProductCondition(),
			entity.getInterestCount(),
			entity.getGenreId()
		);
	}
}
