package com.napzak.domain.product.core.entity;


import com.napzak.domain.genre.core.entity.GenreEntity;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.store.core.entity.StoreEntity;
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

    @Column(name = COLUMN_INTEREST_COUNT, nullable = false)
    private Integer interestCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_TRADE_TYPE, nullable = false)
    private TradeType tradeType;

    @Column(name = COLUMN_PRICE, nullable = false)
    private Integer price;

    @Column(name = COLUMN_CREATED_AT, nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = COLUMN_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = COLUMN_VIEW_COUNT, nullable = false)
    private Integer viewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_TRADE_STATUS, nullable = false)
    private TradeStatus tradeStatus;

    @Column(name = COLUMN_IS_PRICE_NEGOTIABLE, nullable = true)
    private Boolean isPriceNegotiable;

    @Column(name = COLUMN_IS_DELIVERY_INCLUDED, nullable = true)
    private Boolean isDeliveryIncluded;

    @Column(name = COLUMN_STANDARD_DELIVERY_FEE, nullable = true)
    private Integer standardDeliveryFee;

    @Column(name = COLUMN_HALF_DELIVERY_FEE, nullable = true)
    private Integer halfDeliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_PRODUCT_CONDITION, nullable = true)
    private ProductCondition productCondition;

    @ManyToOne
    @JoinColumn(name = COLUMN_STORE_ID, nullable = false)
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = COLUMN_GENRE_ID, nullable = false)
    private GenreEntity genreEntity;

    @Builder
    private ProductEntity(Long id, String title, String description, Integer interestCount, TradeType tradeType, Integer price, TradeStatus tradeStatus, Boolean isPriceNegotiable, Boolean isDeliveryIncluded, Integer standardDeliveryFee, Integer halfDeliveryFee, ProductCondition productCondition, StoreEntity storeEntity, GenreEntity genreEntity
    ){
        this.id = id;
        this.title = title;
        this.description = description;
        this.interestCount = interestCount;
        this.tradeType = tradeType;
        this.price = price;
        this.tradeStatus = tradeStatus;
        this.isPriceNegotiable = isPriceNegotiable;
        this.isDeliveryIncluded = isDeliveryIncluded;
        this.standardDeliveryFee = standardDeliveryFee;
        this.halfDeliveryFee = halfDeliveryFee;
        this.productCondition = productCondition;
        this.storeEntity = storeEntity;
        this.genreEntity = genreEntity;
    }


    public static ProductEntity create(
            final String title,
            final StoreEntity storeEntity,
            final String description,
            final Integer interestCount,
            final TradeType tradeType,
            final TradeStatus tradeStatus,
            final Integer price,
            final Boolean isPriceNegotiable,
            final Boolean isDeliveryIncluded,
            final Integer standardDeliveryFee,
            final Integer halfDeliveryFee,
            final ProductCondition productCondition,
            final GenreEntity genreEntity
    ) {
        return ProductEntity.builder()
                .title(title)
                .storeEntity(storeEntity)
                .description(description)
                .interestCount(interestCount)
                .tradeType(tradeType)
                .tradeStatus(tradeStatus)
                .price(price)
                .isPriceNegotiable(isPriceNegotiable)
                .isDeliveryIncluded(isDeliveryIncluded)
                .standardDeliveryFee(standardDeliveryFee)
                .halfDeliveryFee(halfDeliveryFee)
                .productCondition(productCondition)
                .genreEntity(genreEntity)
                .build();
    }


}
