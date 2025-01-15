package com.napzak.domain.product.core.vo;

import com.napzak.domain.genre.core.entity.GenreEntity;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.store.core.entity.StoreEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {
    private final Long id;
    private final StoreEntity store;
    private final String title;
    private final String description;
    private final Integer interestCount;
    private final TradeType tradeType;
    private final TradeStatus tradeStatus;
    private final Integer price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Integer viewCount;
    private final Boolean isPriceNegotiable;
    private final Boolean isDeliveryIncluded;
    private final Integer standardDeliveryFee;
    private final Integer halfDeliveryFee;
    private final ProductCondition productCondition;
    private final GenreEntity genre;

    public Product(Long id, StoreEntity store, String title, String description, Integer interestCount, TradeType tradeType, TradeStatus tradeStatus, Integer price, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount, Boolean isPriceNegotiable, Boolean isDeliveryIncluded, Integer standardDeliveryFee, Integer halfDeliveryFee, ProductCondition productCondition, GenreEntity genre) {
        this.id = id;
        this.store = store;
        this.title = title;
        this.description = description;
        this.interestCount = interestCount;
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
        this.genre = genre;
    }

    public static Product fromEntity(ProductEntity productEntity){
        return new Product(
                productEntity.getId(),
                productEntity.getStoreEntity(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getInterestCount(),
                productEntity.getTradeType(),
                productEntity.getTradeStatus(),
                productEntity.getPrice(),
                productEntity.getCreatedAt(),
                productEntity.getUpdatedAt(),
                productEntity.getViewCount(),
                productEntity.getIsPriceNegotiable(),
                productEntity.getIsDeliveryIncluded(),
                productEntity.getStandardDeliveryFee(),
                productEntity.getHalfDeliveryFee(),
                productEntity.getProductCondition(),
                productEntity.getGenreEntity()
        );
    }
}
