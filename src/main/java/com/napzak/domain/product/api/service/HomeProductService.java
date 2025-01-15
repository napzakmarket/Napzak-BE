package com.napzak.domain.product.api.service;

import com.napzak.domain.product.api.dto.HomeProductResponse;
import com.napzak.domain.product.core.ProductPhotoRepository;
import com.napzak.domain.product.core.ProductPhotoRetriever;
import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeProductService {

    private final ProductRetriever productRetriever;
    private final ProductService productService;

    public List<HomeProductResponse> getRecommendedProducts(Long storeId){
        Pageable pageable = PageRequest.of(0, 2);

        List<Product> buyProducts = productRetriever.findRecommendedProducts(storeId, TradeType.BUY, pageable);
        List<Product> sellProducts = productRetriever.findRecommendedProducts(storeId, TradeType.SELL, pageable);

        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(buyProducts);
        allProducts.addAll(sellProducts);

        return homeProductResponseGenerator(allProducts, storeId);
    }

    public List<HomeProductResponse> getSpecificProducts(Long storeId){

        //산리오 : 2 , 건담 : 8 , 디즈니/픽사 : 5 , 원피스 : 6
        List<Integer> genreIds = List.of(2, 8, 5, 6);
        List<TradeType> tradeTypes = List.of(TradeType.SELL, TradeType.BUY, TradeType.SELL, TradeType.BUY);

        List<Product> allProducts = new ArrayList<>();

        // 배열 지양
        for (int i = 0; i < genreIds.size(); i++) {
            allProducts.add(productRetriever.findTopProductByGenreIdAndTradeType(Long.valueOf(genreIds.get(i)), tradeTypes.get(i), storeId));
        }


        return homeProductResponseGenerator(allProducts, storeId);
    }

    private List<HomeProductResponse> homeProductResponseGenerator(List<Product> products, Long storeId){

        List<HomeProductResponse> homeProductResponses = new ArrayList<>();
        for (Product product : products){
            String photo =  productService.getFirstSequencePhoto(product.getId());
            String uploadTime = productService.calculateUploadTime(product.getCreatedAt());
            Boolean isInterested = productService.getIsInterested(storeId, product.getId());

            homeProductResponses.add(HomeProductResponse.of(product, photo, uploadTime, isInterested));
        }

        return homeProductResponses;
    }
}
