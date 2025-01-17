package com.napzak.domain.product.core.vo;

import java.time.LocalDateTime;

import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;

import lombok.Getter;

@Getter
public class ProductWithFirstPhoto {
	private final Long id;
	private final String title;
	private final int price;
	private final int interestCount;
	private final LocalDateTime createdAt;
	private final String firstPhoto;
	private final Long genreId;
	private final TradeType tradeType;
	private final TradeStatus tradeStatus;
	private final boolean isPriceNegotiable;

	private ProductWithFirstPhoto(Long id, String title, int price, int interestCount,
		LocalDateTime createdAt, String firstPhoto, Long genreId,
		TradeType tradeType, TradeStatus tradeStatus, boolean isPriceNegotiable) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.interestCount = interestCount;
		this.createdAt = createdAt;
		this.firstPhoto = firstPhoto;
		this.genreId = genreId;
		this.tradeType = tradeType;
		this.tradeStatus = tradeStatus;
		this.isPriceNegotiable = isPriceNegotiable;
	}

	public static ProductWithFirstPhoto from(Product product, String firstPhoto) {
		return new ProductWithFirstPhoto(
			product.getId(),
			product.getTitle(),
			product.getPrice(),
			product.getInterestCount(),
			product.getCreatedAt(),
			firstPhoto,
			product.getGenreId(),
			product.getTradeType(),
			product.getTradeStatus(),
			product.getIsPriceNegotiable()
		);
	}
}
