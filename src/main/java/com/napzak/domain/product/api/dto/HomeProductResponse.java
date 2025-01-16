package com.napzak.domain.product.api.dto;

import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;

public record HomeProductResponse(
        Long productId,
        String genreName,
        String productName,
        String photo,
        int price,
        String uploadTime,
        Boolean isInterested,
        String tradeType,
        String tradeStatus,
        Boolean isPriceNegotiable
) {

    public static HomeProductResponse of(
            Long productId,
            String genreName,
            String productName,
            String photo,
            int price,
            String uploadTime,
            Boolean isInterested,
            String tradeType,
            String tradeStatus,
            Boolean isPriceNegotiable){


        return new HomeProductResponse(
                productId,
                genreName,
                productName,
                photo,
                price,
                uploadTime,
                isInterested,
                tradeType,
                tradeStatus,
                isPriceNegotiable);
    }

}
