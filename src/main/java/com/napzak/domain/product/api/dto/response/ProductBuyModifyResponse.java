package com.napzak.domain.product.api.dto.response;

import java.util.List;

public record ProductBuyModifyResponse(
	Long productId,
	Long genreId,
	String genreName,
	String title,
	String description,
	int price,
	boolean isPriceNegotiable,
	List<ProductPhotoDto> productPhotoList
) {
	public static ProductBuyModifyResponse from(
		Long productId,
		Long genreId,
		String genreName,
		String title,
		String description,
		int price,
		boolean isPriceNegotiable,
		List<ProductPhotoDto> productPhotoList
	) {
		return new ProductBuyModifyResponse(productId, genreId, genreName, title, description, price, isPriceNegotiable,
			productPhotoList);
	}
}
