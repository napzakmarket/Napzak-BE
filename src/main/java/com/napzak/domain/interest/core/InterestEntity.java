package com.napzak.domain.interest.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.interest.core.InterestTableConstants.*;


@Table(name = TABLE_INTEREST)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = COLUMN_STORE_ID, nullable = false)
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity productEntity;

    @Builder
    public InterestEntity(
            Long id,
            StoreEntity storeEntity,
            ProductEntity productEntity) {
        this.id = id;
        this.storeEntity = storeEntity;
        this.productEntity = productEntity;
    }


    public static InterestEntity create(
            final StoreEntity storeEntity,
            final ProductEntity productEntity
            ){
            return InterestEntity.builder()
                    .storeEntity(storeEntity)
                    .productEntity(productEntity)
                    .build();
    }
}
