package com.napzak.api.domain.product.dto.response;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.api.domain.product.service.ProductPagination;
import com.napzak.domain.product.entity.enums.ProductSortOption;
import com.napzak.common.util.TimeUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductBuyListResponse(
	Integer productCount,
	List<ProductBuyDto> productBuyList,
	@Nullable
	String nextCursor
) {
	public static ProductBuyListResponse from(
		Integer productCount,
		ProductSortOption productSortOption,
		ProductPagination pagination,
		Map<Long, Boolean> interestMap,
		Map<Long, String> genreMap,
		Long currentStoreId
	) {
		// 1. DTO 생성
		List<ProductBuyDto> productDtos = pagination.getProductList().stream()
			.map(product -> {
				String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt());
				boolean isInterested = interestMap.getOrDefault(product.getId(), false);
				String genreName = genreMap.getOrDefault(product.getGenreId(), "기타");
				boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId());

				return ProductBuyDto.from(
					product, uploadTime, isInterested, genreName, isOwnedByCurrentUser
				);
			}).toList();

		// 2. Next Cursor 생성
		String nextCursor = pagination.generateNextCursor(productSortOption);

		return new ProductBuyListResponse(productCount, productDtos, nextCursor);
	}

	public static ProductBuyListResponse fromWithoutCursor(
		Integer productCount,
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

		return new ProductBuyListResponse(productCount, productDtos, null);
	}

	public List<ProductBuyDto> getProductBuyList() {
		return productBuyList;
	}

	public String getNextCursor() {
		return nextCursor;
	}
}
