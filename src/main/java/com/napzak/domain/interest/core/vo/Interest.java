package com.napzak.domain.interest.core.vo;

import com.napzak.domain.interest.core.entity.InterestEntity;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.store.core.entity.StoreEntity;
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
