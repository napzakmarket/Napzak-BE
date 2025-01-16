package com.napzak.domain.product.core;


import com.napzak.domain.product.api.ProductInterestFacade;
import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.global.common.exception.NapzakException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRetriever {

    private final ProductRepository productRepository;
    private final ProductInterestFacade productInterestFacade;
    private final ProductPhotoRetriever productPhotoRetriever;

    //사용자가 특정 product에 좋아요를 눌렀는지 boolean 값을 받아오는 메서드
    public Boolean getIsInterested(Long storeId, Long productId){
        return productInterestFacade.getIsInterested(storeId, productId);
    }

    /*
    //첫번째 순서의 사진(대표사진)의 url을 String으로 가져오는 메서드
    public String getFirstSequencePhoto(Long productId){
        return productPhotoRetriever.findFirstSequencePicture(productId);
    }
     */

    //LocalDateTime createdAt을 대략적으로 표현하는(e.g. n일전) 메서드
    @Transactional(readOnly = true)
    public String calculateUploadTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "방금";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "분";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간";
        } else if (duration.toDays() < 31) {
            return duration.toDays() + "일";
        } else if (duration.toDays() < 365) {
            return (duration.toDays() / 30) + "달";
        } else {
            return (duration.toDays() / 365) + "년";
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findRecommendedProductsByStoreIdAndTradeType(Long socialId, TradeType tradeType){

        Pageable pageable = PageRequest.of(0, 2);
        List<ProductEntity> productEntityList = productRepository.findRecommendedProductsByStoreIdAndTradeType(socialId, tradeType.toString(), pageable);

        return productEntityList.stream()
                .map(Product::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Product findTopProductByGenreIdAndTradeType(Long genreId, TradeType tradeType, Long storeId){

        Pageable pageable = PageRequest.of(0,1);
        List<ProductEntity> productEntityList = productRepository.findTopSpecificProductByGenreIdAndTradeType(genreId, tradeType, storeId, pageable);

        if(productEntityList.isEmpty()){
            return null;
        }

        return Product.fromEntity(productEntityList.get(0));
    }

    @Transactional(readOnly = true)
    public List<Product> findTopProductsByStoreIdAndTradeType(Long storeId, TradeType tradeType){

        Pageable pageable = PageRequest.of(0, 4);
        List<ProductEntity> productEntityList = productRepository.findTopInterestProductsByStoreIDAndTradeType(storeId, tradeType, pageable);

        return productEntityList.stream()
                .map(Product::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Product findProductById(Long productId){
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(()-> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return Product.fromEntity(productEntity);
    }
}
