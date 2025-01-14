package com.napzak.domain.product.core;

import com.napzak.domain.genre.core.GenreEntity;
import com.napzak.domain.product.core.enums.Condition;
import com.napzak.domain.product.core.enums.TradeStatus;
import com.napzak.domain.product.core.enums.TradeType;
import com.napzak.domain.store.core.StoreEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {
    private final Long id;
    private final StoreEntity store;
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
    private final Condition condition;
    private final GenreEntity genre;

    public Product(Long id, StoreEntity store, String title, String description, TradeType tradeType, TradeStatus tradeStatus, int price, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, Boolean isPriceNegotiable, Boolean isDeliveryIncluded, int standardDeliveryFee, int halfDeliveryFee, Condition condition, GenreEntity genre) {
        this.id = id;
        this.store = store;
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
        this.condition = condition;
        this.genre = genre;
    }

    public static Product fromEntity(ProductEntity productEntity){
        return new Product(
                productEntity.getId(),
                productEntity.getStore(),
                productEntity.getTitle(),
                productEntity.getDescription(),
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
                productEntity.getCondition(),
                productEntity.getGenre()
        );
    }
}
