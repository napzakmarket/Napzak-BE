package com.napzak.domain.product.core;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.entity.ProductPhotoEntity;
import com.napzak.domain.product.core.vo.ProductPhoto;
import com.napzak.global.common.exception.NapzakException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductPhotoRetriever {

    final ProductPhotoRepository productPhotoRepository;

    @Transactional(readOnly = true)
    public String findFirstSequencePicture(Long productId){
        ProductPhotoEntity productPhotoEntity = productPhotoRepository.findFirstSequencePhotoByProductId(productId)
                .orElseThrow(()-> new NapzakException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND));
        return ProductPhoto.fromEntity(productPhotoEntity).toString();
    }
}
