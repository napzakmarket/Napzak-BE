package com.napzak.domain.like.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Like {
    private final Long id;
    private final StoreEntity store;
    private final ProductEntity product;

    public Like(Long id, StoreEntity store, ProductEntity product) {
        this.id = id;
        this.store = store;
        this.product = product;
    }

    public static Like fromEntity(LikeEntity likeEntity) {
        return new Like(
                likeEntity.getId(),
                likeEntity.getStore(),
                likeEntity.getProduct()
        );
    }
}
