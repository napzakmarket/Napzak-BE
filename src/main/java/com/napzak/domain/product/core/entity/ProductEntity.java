package com.napzak.domain.product.core.entity;

import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.product.core.entity.ProductTableConstants.*;

@Table(name = TABLE_PRODUCT)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_TITLE, nullable = false)
	private String title;

	@Column(name = COLUMN_DESCRIPTION, nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_TRADE_TYPE, nullable = false)
	private TradeType tradeType;

	@Column(name = COLUMN_PRICE, nullable = false)
	private int price;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = COLUMN_UPDATED_AT, nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now();

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
	@Column(name = COLUMN_PRODUCT_CONDITION, nullable = false)
	private ProductCondition productCondition;

	@Column(name = COLUMN_INTEREST_COUNT, nullable = false)
	private int interestCount = 0;

	@Column(name = COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Column(name = COLUMN_GENRE_ID, nullable = false)
	private Long genreId;

	@Builder
	private ProductEntity(Long id, String title, String description, TradeType tradeType, int price,
		TradeStatus tradeStatus, Boolean isPriceNegotiable, Boolean isDeliveryIncluded,
		int standardDeliveryFee, int halfDeliveryFee, ProductCondition productCondition,
		Long storeId, Long genreId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.tradeType = tradeType;
		this.price = price;
		this.tradeStatus = tradeStatus;
		this.isPriceNegotiable = isPriceNegotiable;
		this.isDeliveryIncluded = isDeliveryIncluded;
		this.standardDeliveryFee = standardDeliveryFee;
		this.halfDeliveryFee = halfDeliveryFee;
		this.productCondition = productCondition;
		this.storeId = storeId;
		this.genreId = genreId;
	}

	public static ProductEntity create(
		final String title,
		final Long storeId,
		final String description,
		final TradeType tradeType,
		final TradeStatus tradeStatus,
		final int price,
		final Boolean isPriceNegotiable,
		final Boolean isDeliveryIncluded,
		final int standardDeliveryFee,
		final int halfDeliveryFee,
		final ProductCondition productCondition,
		final Long genreId
	) {
		return ProductEntity.builder()
			.title(title)
			.storeId(storeId)
			.description(description)
			.tradeType(tradeType)
			.tradeStatus(tradeStatus)
			.price(price)
			.isPriceNegotiable(isPriceNegotiable)
			.isDeliveryIncluded(isDeliveryIncluded)
			.standardDeliveryFee(standardDeliveryFee)
			.halfDeliveryFee(halfDeliveryFee)
			.productCondition(productCondition)
			.genreId(genreId)
			.build();
	}
}