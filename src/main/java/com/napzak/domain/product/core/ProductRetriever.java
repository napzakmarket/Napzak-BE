package com.napzak.domain.product.core;


import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRetriever {

    private final ProductRepository productRepository;

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
}
