package com.napzak.domain.product.api.dto;

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
            Product product,
            String photo,
            String uploadTime,
            Boolean isInterested){

        final Long productId = product.getId();
        final String genreName = product.getGenre().getName();
        final String productName = product.getTitle();
        final int price = product.getPrice();
        final String tradeType = product.getTradeType().toString();
        final String tradeStatus = product.getTradeStatus().toString();
        final Boolean isPriceNegotiable = product.getIsPriceNegotiable();

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
