package com.napzak.domain.like.core;

import com.napzak.domain.product.core.Product;
import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.like.core.LikeTableConstants.*;


@Table(name = TABLE_LIKE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_STORE_ID, nullable = false)
    private StoreEntity store;

    @Column(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity product;

    @Builder
    public LikeEntity(
            Long id,
            StoreEntity store,
            ProductEntity product) {
        this.id = id;
        this.store = store;
        this.product = product;
    }


    public static LikeEntity create(
            final StoreEntity store,
            final ProductEntity product
            ){
            return LikeEntity.builder()
                    .store(store)
                    .product(product)
                    .build();
    }
}
