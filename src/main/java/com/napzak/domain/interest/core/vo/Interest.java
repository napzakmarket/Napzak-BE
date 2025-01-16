package com.napzak.domain.interest.core.vo;

import com.napzak.domain.interest.core.entity.InterestEntity;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.store.core.entity.StoreEntity;
import lombok.Getter;

@Getter
public class Interest {
    private final Long id;
    private final Long storeId;
    private final Long productId;

    public Interest(Long id, Long storeId, Long productId) {
        this.id = id;
        this.storeId = storeId;
        this.productId = productId;
    }

    public static Interest fromEntity(InterestEntity interestEntity) {
        return new Interest(
                interestEntity.getId(),
                interestEntity.getStoreId(),
                interestEntity.getProductId()
        );
    }
}
