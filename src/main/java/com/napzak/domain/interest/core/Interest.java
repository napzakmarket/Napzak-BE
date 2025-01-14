package com.napzak.domain.interest.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import lombok.Getter;

@Getter
public class Interest {
    private final Long id;
    private final StoreEntity store;
    private final ProductEntity product;

    public Interest(Long id, StoreEntity store, ProductEntity product) {
        this.id = id;
        this.store = store;
        this.product = product;
    }

    public static Interest fromEntity(InterestEntity interestEntity) {
        return new Interest(
                interestEntity.getId(),
                interestEntity.getStore(),
                interestEntity.getProduct()
        );
    }
}
