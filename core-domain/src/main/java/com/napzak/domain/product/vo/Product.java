package com.napzak.domain.product.vo;

import com.napzak.domain.product.entity.enums.ProductCondition;
import com.napzak.domain.product.entity.enums.TradeStatus;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.entity.ProductEntity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {
	private final Long id;
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
	private final int chatCount;
	private final Boolean isVisible;
	private final Long storeId;
	private final Long genreId;

	public Product(Long id, String title, String description, TradeType tradeType,
		TradeStatus tradeStatus, int price, LocalDateTime createdAt, LocalDateTime updatedAt,
		int viewCount, Boolean isPriceNegotiable, Boolean isDeliveryIncluded,
		int standardDeliveryFee, int halfDeliveryFee, ProductCondition productCondition,
		int interestCount, int chatCount, Boolean isVisible, Long storeId, Long genreId) {
		this.id = id;
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
		this.chatCount = chatCount;
		this.isVisible = isVisible;
		this.storeId = storeId;
		this.genreId = genreId;
	}

	public static Product fromEntity(ProductEntity entity) {
		return new Product(
			entity.getId(),
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
			entity.getChatCount(),
			entity.getIsVisible(),
			entity.getStoreId(),
			entity.getGenreId()
		);
	}
}
