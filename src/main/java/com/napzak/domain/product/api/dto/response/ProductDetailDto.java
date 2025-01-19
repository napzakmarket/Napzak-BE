package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.entity.enums.TradeType;

public record ProductDetailDto(
	Long productId,
	TradeType tradeType,
	String genreName,
	String productName,
	int price,
	String uploadTime,
	int viewCount,
	int interestCount,
	String description,
	String productCondition,
	int standardDeliveryFee,
	int halfDeliveryFee,
	boolean is_delivery_included,
	boolean isPriceNegotiable,
	String tradeStatus,
	boolean isOwnedByCurrentUser
) {
	public static ProductDetailDto from(

	)
}

/*
public record StoreLoginResponse(
        String accessToken,
        String nickname,
        Role role
) {
    public static StoreLoginResponse of(
            final String accessToken,
            String nickname,
            final Role role
    ) {
        return new StoreLoginResponse(accessToken, nickname, role);
    }
 */
