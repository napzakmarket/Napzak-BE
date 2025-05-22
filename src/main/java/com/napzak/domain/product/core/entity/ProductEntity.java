package com.napzak.domain.product.core.entity;

import static com.napzak.domain.product.core.entity.ProductTableConstants.*;

import java.time.LocalDateTime;

import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_PRODUCT)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_TITLE, nullable = false, columnDefinition = "varchar(50)")
	private String title;

	@Column(name = COLUMN_DESCRIPTION, nullable = false, columnDefinition = "varchar(250)")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_TRADE_TYPE, nullable = false)
	private TradeType tradeType;

	@Column(name = COLUMN_PRICE, nullable = false)
	private int price;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = COLUMN_UPDATED_AT, nullable = true)
	private LocalDateTime updatedAt;

	@Column(name = COLUMN_VIEW_COUNT, nullable = false)
	private int viewCount = 0;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_TRADE_STATUS, nullable = false)
	private TradeStatus tradeStatus;

	@Column(name = COLUMN_IS_PRICE_NEGOTIABLE, nullable = false)
	private Boolean isPriceNegotiable = false;

	@Column(name = COLUMN_IS_DELIVERY_INCLUDED, nullable = false)
	private Boolean isDeliveryIncluded = false;

	@Column(name = COLUMN_STANDARD_DELIVERY_FEE, nullable = false)
	private int standardDeliveryFee = 0;

	@Column(name = COLUMN_HALF_DELIVERY_FEE, nullable = false)
	private int halfDeliveryFee = 0;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_PRODUCT_CONDITION, nullable = true)
	private ProductCondition productCondition;

	@Column(name = COLUMN_INTEREST_COUNT, nullable = false)
	private int interestCount = 0;

	@Column(name = COLUMN_CHAT_COUNT, nullable = false)
	private int chatCount = 0;

	@Column(name = COLUMN_IS_VISIBLE, nullable = false)
	private Boolean isVisible = true;

	@Column(name = COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Column(name = COLUMN_GENRE_ID, nullable = false)
	private Long genreId;

	@Builder
	private ProductEntity(String title, String description, TradeType tradeType, int price,
		LocalDateTime updatedAt, int viewCount,
		TradeStatus tradeStatus, Boolean isPriceNegotiable, Boolean isDeliveryIncluded,
		int standardDeliveryFee, int halfDeliveryFee, ProductCondition productCondition,
		int interestCount, int chatCount, boolean isVisible,
		Long storeId, Long genreId) {
		this.title = title;
		this.description = description;
		this.tradeType = tradeType;
		this.price = price;
		this.updatedAt = updatedAt;
		this.viewCount = viewCount;
		this.tradeStatus = tradeStatus;
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

	public static ProductEntity create(
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
		final Long genreId,
		final Boolean isVisible
	) {
		return ProductEntity.builder()
			.title(title)
			.description(description)
			.tradeType(tradeType)
			.tradeStatus(tradeStatus)
			.price(price)
			.isPriceNegotiable(isPriceNegotiable)
			.isDeliveryIncluded(isDeliveryIncluded)
			.standardDeliveryFee(standardDeliveryFee)
			.halfDeliveryFee(halfDeliveryFee)
			.productCondition(productCondition)
			.storeId(storeId)
			.genreId(genreId)
			.isVisible(isVisible)
			.build();
	}

	public void update(
		final String title,
		final String description,
		final int price,
		final Boolean isPriceNegotiable,
		final Boolean isDeliveryIncluded,
		final int standardDeliveryFee,
		final int halfDeliveryFee,
		final ProductCondition productCondition,
		final Long genreId
	) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.isPriceNegotiable = isPriceNegotiable;
		this.isDeliveryIncluded = isDeliveryIncluded;
		this.standardDeliveryFee = standardDeliveryFee;
		this.halfDeliveryFee = halfDeliveryFee;
		this.productCondition = productCondition;
		this.genreId = genreId;
		this.updatedAt = LocalDateTime.now();
	}
}