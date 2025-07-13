package com.napzak.api.domain.product.dto.response;

import java.util.List;
import java.util.Map;

import com.napzak.api.domain.product.service.ProductPagination;
import com.napzak.common.util.TimeUtils;

public record ProductRecommendListResponse(
	String nickname,
	List<ProductBuyDto> productRecommendList
) {
	public static ProductRecommendListResponse from(
		String nickname,
		ProductPagination pagination,
		Map<Long, Boolean> interestMap,
		Map<Long, String> genreMap,
		Long currentStoreId
	) {
		List<ProductBuyDto> productDtos = pagination.getProductList().stream()
			.map(product -> {
				String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt());
				boolean isInterested = interestMap.getOrDefault(product.getId(), false);
				String genreName = genreMap.getOrDefault(product.getGenreId(), "기타"); // genreName 매핑
				boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId());

				return ProductBuyDto.from(
					product, uploadTime, isInterested, genreName, isOwnedByCurrentUser
				);
			}).toList();

		return new ProductRecommendListResponse(nickname, productDtos);
	}
}