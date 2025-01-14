package com.napzak.domain.product.core;

import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.product.core.ProductHashtagTableConstants.*;

@Entity
@Table(name = TABLE_PRODUCT_HASHTAG)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductHashtagEntity {

    @Id
    @Column(name = COLUMN_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = COLUMN_HASHTAG_ID, nullable = false)
    private HashtagEntity hashtag;

    @Builder
    private ProductHashtagEntity(ProductEntity product, HashtagEntity hashtag) {
        this.product = product;
        this.hashtag = hashtag;
    }

    public static ProductHashtagEntity create(
            final ProductEntity product,
            final HashtagEntity hashtag
    ) {
        return ProductHashtagEntity.builder()
                .product(product)
                .hashtag(hashtag)
                .build();
    }
}