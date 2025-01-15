package com.napzak.domain.interest.core.entity;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.store.core.entity.StoreEntity;
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
