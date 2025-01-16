package com.napzak.domain.interest.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.interest.core.entity.InterestTableConstants.*;


@Table(name = TABLE_INTEREST)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_STORE_ID, nullable = false)
    private Long storeId;

    @Column(name = COLUMN_PRODUCT_ID, nullable = false)
    private Long productId;

    @Builder
    public InterestEntity(
            Long id,
            Long storeId,
            Long productId) {
        this.id = id;
        this.storeId = storeId;
        this.productId = productId;
    }


    public static InterestEntity create(
            final Long storeId,
            final Long productId
            ){
            return InterestEntity.builder()
                    .storeId(storeId)
                    .productId(productId)
                    .build();
    }
}
