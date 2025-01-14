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
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity product;

    @Builder
    public InterestEntity(
            Long id,
            StoreEntity store,
            ProductEntity product) {
        this.id = id;
        this.store = store;
        this.product = product;
    }


    public static InterestEntity create(
            final StoreEntity store,
            final ProductEntity product
            ){
            return InterestEntity.builder()
                    .store(store)
                    .product(product)
                    .build();
    }
}
