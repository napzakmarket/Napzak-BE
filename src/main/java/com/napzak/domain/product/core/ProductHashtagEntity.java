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
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = COLUMN_HASHTAG_ID, nullable = false)
    private HashtagEntity hashtagEntity;

    @Builder
    private ProductHashtagEntity(ProductEntity productEntity, HashtagEntity hashtagEntity) {
        this.productEntity = productEntity;
        this.hashtagEntity = hashtagEntity;
    }

    public static ProductHashtagEntity create(
            final ProductEntity productEntity,
            final HashtagEntity hashtagEntity
    ) {
        return ProductHashtagEntity.builder()
                .productEntity(productEntity)
                .hashtagEntity(hashtagEntity)
                .build();
    }
}