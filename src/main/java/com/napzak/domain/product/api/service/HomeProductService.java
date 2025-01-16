package com.napzak.domain.product.api.service;

import com.napzak.domain.banner.core.BannerRetriever;
import com.napzak.domain.banner.core.vo.Banner;
import com.napzak.domain.product.api.dto.HomeBannerResponse;
import com.napzak.domain.product.api.dto.HomeProductResponse;
import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeProductService {

    private final ProductRetriever productRetriever;
    private final BannerRetriever bannerRetriever;
    private final ProductService productService;

    //선호 장르가 있을 때, 로그인한 사용자의 선호장르에 속하는 SELL, BUY Product 2개씩. 노출기준 : 최신순, 선호장르
    public List<HomeProductResponse> getRecommendedProducts(Long storeId){

        List<Product> buyProducts = productRetriever.findRecommendedProductsByStoreIdAndTradeType(storeId, TradeType.BUY);
        List<Product> sellProducts = productRetriever.findRecommendedProductsByStoreIdAndTradeType(storeId, TradeType.SELL);

        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(buyProducts);
        allProducts.addAll(sellProducts);

        return homeProductResponseGenerator(allProducts, storeId);
    }

    //선호 장르가 없을 때, 미리 지정해 둔 장르의 Product 4개. 노출기준 : 인기순
    public List<HomeProductResponse> getSpecificProducts(Long storeId){

        //산리오 : 2 , 건담 : 8 , 디즈니/픽사 : 5 , 원피스 : 6
        List<Integer> genreIds = List.of(2, 8, 5, 6);
        List<TradeType> tradeTypes = List.of(TradeType.SELL, TradeType.BUY, TradeType.SELL, TradeType.BUY);

        List<Product> allProducts = new ArrayList<>();

        for (int i = 0; i < genreIds.size(); i++) {

            Product product = productRetriever.findTopProductByGenreIdAndTradeType(Long.valueOf(genreIds.get(i)), tradeTypes.get(i), storeId);

            if (product != null) {
                allProducts.add(product);
            }
        }



        return homeProductResponseGenerator(allProducts, storeId);
    }

    //SELL 상품 4개. 노출기준 : 인기순
    public List<HomeProductResponse> getTopSellProducts(Long storeId){

        List<Product> allProducts = productRetriever.findTopProductsByStoreIdAndTradeType(storeId, TradeType.SELL);

        return homeProductResponseGenerator(allProducts, storeId);
    }

    //BUY 상품 4개. 노출기준 : 인기순
    public List<HomeProductResponse> getTopBuyProducts(Long storeId){

        List<Product> allProducts = productRetriever.findTopProductsByStoreIdAndTradeType(storeId, TradeType.BUY);

        return homeProductResponseGenerator(allProducts,storeId);
    }


    public List<HomeBannerResponse> getAllBanners(){

        List<Banner> allBanners = bannerRetriever.findAllBanners();

        return allBanners.stream()
                .map(banner -> HomeBannerResponse.of(
                        banner.getId(),
                        banner.getPhotoUrl(),
                        banner.getRedirectUrl(),
                        banner.getSequence()
                ))
                .toList();
    }

    //List<Product>를 받아 대표이미지, 시간 표기, 좋아요 여부를 반영해 response를 생성
    private List<HomeProductResponse> homeProductResponseGenerator(List<Product> products, Long storeId){

        List<HomeProductResponse> homeProductResponses = new ArrayList<>();
        for (Product product : products){
            String photo =  productService.getFirstSequencePhoto(product.getId());
            String uploadTime = productService.calculateUploadTime(product.getCreatedAt());
            Boolean isInterested = productService.getIsInterested(storeId, product.getId());

            homeProductResponses.add(HomeProductResponse.of(
                    product.getId(),
                    product.getGenre().toString(),
                    product.getTitle(),
                    photo,
                    product.getPrice(),
                    uploadTime,
                    isInterested,
                    product.getTradeType().toString(),
                    product.getTradeStatus().toString(),
                    product.getIsPriceNegotiable()));
        }

        return homeProductResponses;
    }
}
