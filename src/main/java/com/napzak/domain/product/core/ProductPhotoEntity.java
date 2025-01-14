package com.napzak.domain.product.core;

import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.product.core.ProductPhotoTableConstants.*;

@Table(name = TABLE_PRODUCT_PHOTO)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPhotoEntity {

    @Id
    @Column(name = COLUMN_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity product;

    @Column(name = COLUMN_PHOTO_URL, nullable = false)
    private String photoUrl;

    @Column(name = COLUMN_ORDER, nullable = false)
    private int order;

    @Builder
    private  ProductPhotoEntity(Long id, ProductEntity product, String photoUrl, int order){
        this.id = id;
        this.product = product;
        this.photoUrl = photoUrl;
        this.order = order;
    }

    public static ProductPhotoEntity create(
            final ProductEntity product,
            final String photoUrl,
            final int order
    ){
            return ProductPhotoEntity.builder()
                    .product(product)
                    .photoUrl(photoUrl)
                    .order(order)
                    .build();
    }

}
