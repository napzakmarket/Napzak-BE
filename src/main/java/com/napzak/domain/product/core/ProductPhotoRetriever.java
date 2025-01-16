package com.napzak.domain.product.core;

import com.napzak.domain.product.api.dto.ProductPhotoDto;
import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.ProductPhotoEntity;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.domain.product.core.vo.ProductPhoto;
import com.napzak.global.common.exception.NapzakException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductPhotoRetriever {

    final ProductPhotoRepository productPhotoRepository;

    /*
    @Transactional(readOnly = true)
    public String findFirstSequencePicture(Long productId){
        ProductPhotoEntity productPhotoEntity = productPhotoRepository.findFirstSequencePhotoByProductId(productId)
                .orElseThrow(()-> new NapzakException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND));
        return ProductPhoto.fromEntity(productPhotoEntity).toString();
    }
     */

    @Transactional(readOnly = true)
    public List<ProductPhotoDto> findFistSequencePhotos(List<Long> productIds){
        List<ProductPhotoDto> firstPhotoUrls = productPhotoRepository.findFirstSequencePhotosByProductIds(productIds);

        if(firstPhotoUrls.isEmpty()){
            throw new NapzakException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND);
        }

        return firstPhotoUrls;

    }
}
