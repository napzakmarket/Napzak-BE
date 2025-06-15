package com.napzak.domain.interest.core.vo;

import java.time.LocalDateTime;
import com.napzak.domain.interest.core.entity.InterestEntity;
import lombok.Getter;

@Getter
public class Interest {
    private final Long id;
    private final Long storeId;
    private final Long productId;
    private final LocalDateTime createAt;

    public Interest(Long id, Long storeId, Long productId, LocalDateTime createdAt) {
        this.id = id;
        this.storeId = storeId;
        this.productId = productId;
        this.createAt = createdAt;
    }

    public static Interest fromEntity(InterestEntity interestEntity) {
        return new Interest(
            interestEntity.getId(),
            interestEntity.getStoreId(),
            interestEntity.getProductId(),
            interestEntity.getCreatedAt()
        );
    }
}