package com.napzak.domain.product.api.dto.response;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.domain.product.api.service.ProductPagination;
import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.global.common.util.TimeUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductSellListResponse(
	Integer productCount,
	List<ProductSellDto> productSellList,
	@Nullable
	String nextCursor
) {
	public static ProductSellListResponse from(
		Integer productCount,
		SortOption sortOption,
		ProductPagination pagination,
		Map<Long, Boolean> interestMap,
		Map<Long, String> genreMap,
		Long currentStoreId
	) {
		// 1. DTO 생성
		List<ProductSellDto> productDtos = pagination.getProductList().stream()
			.map(product -> {
				String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt());
				boolean isInterested = interestMap.getOrDefault(product.getId(), false);
				String genreName = genreMap.getOrDefault(product.getGenreId(), "기타");
				boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId());

				return ProductSellDto.from(
					product, uploadTime, isInterested, genreName, isOwnedByCurrentUser
				);
			}).toList();

		// 2. Next Cursor 생성
		String nextCursor = pagination.generateNextCursor(sortOption);

		return new ProductSellListResponse(productCount, productDtos, nextCursor);
	}

	public static ProductSellListResponse fromWithoutCursor(
		Integer productCount,
		ProductPagination pagination,
		Map<Long, Boolean> interestMap,
		Map<Long, String> genreMap,
		Long currentStoreId
	) {

		List<ProductSellDto> productDtos = pagination.getProductList().stream()
			.map(product -> {
				String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt());
				boolean isInterested = interestMap.getOrDefault(product.getId(), false);
				String genreName = genreMap.getOrDefault(product.getGenreId(), "기타"); // genreName 매핑
				boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId());

				return ProductSellDto.from(
					product, uploadTime, isInterested, genreName, isOwnedByCurrentUser
				);
			}).toList();

		return new ProductSellListResponse(productCount, productDtos, null);
	}

	public List<ProductSellDto> getProductSellList() {
		return productSellList;
	}

	public String getNextCursor() {
		return nextCursor;
	}
}
