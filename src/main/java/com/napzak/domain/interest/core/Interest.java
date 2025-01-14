package com.napzak.domain.interest.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import lombok.Getter;

@Getter
public class Interest {
    private final Long id;
    private final StoreEntity storeEntity;
    private final ProductEntity productEntity;

    public Interest(Long id, StoreEntity storeEntity, ProductEntity productEntity) {
        this.id = id;
        this.storeEntity = storeEntity;
        this.productEntity = productEntity;
    }

    public static Interest fromEntity(InterestEntity interestEntity) {
        return new Interest(
                interestEntity.getId(),
                interestEntity.getStoreEntity(),
                interestEntity.getProductEntity()
        );
    }
}
