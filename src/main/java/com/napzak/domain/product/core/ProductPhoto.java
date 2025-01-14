package com.napzak.domain.product.core;

import com.napzak.domain.store.core.Store;
import lombok.Getter;

@Getter
public class ProductPhoto {
    private final Long id;
    private final Product product;
    private final String photoUrl;
    private final int sequence;

    public ProductPhoto(Long id, Product product, String photoUrl, int sequence) {
        this.id = id;
        this.product = product;
        this.photoUrl = photoUrl;
        this.sequence = sequence;
    }

    public static ProductPhoto fromEntity(ProductPhotoEntity productPhotoEntity) {
        return new ProductPhoto(
                productPhotoEntity.getId(),
                Product.fromEntity(productPhotoEntity.getProduct()),
                productPhotoEntity.getPhotoUrl(),
                productPhotoEntity.getSequence()
        );
    }
}