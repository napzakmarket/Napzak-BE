package com.napzak.domain.product.core.vo;

import com.napzak.domain.product.core.entity.ProductHashtagEntity;
import lombok.Getter;

@Getter
public class ProductHashtag {
    private final Long id;
    private final Product product;
    private final Hashtag hashtag;

    public ProductHashtag(Long id, Product product,  Hashtag hashtag) {
        this.id = id;
        this.product = product;
        this.hashtag = hashtag;
    }

    public static ProductHashtag fromEntity(ProductHashtagEntity productHashtagEntity) {
        return new ProductHashtag(
                productHashtagEntity.getId(),
                Product.fromEntity(productHashtagEntity.getProductEntity()),
                Hashtag.fromEntity(productHashtagEntity.getHashtagEntity())
        );
    }
}