package com.napzak.domain.product.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.domain.product.api.service.ProductPagination;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;
import com.napzak.global.common.util.TimeUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InterestedSellProductResponse(
	List<ProductSellDto> interestedSellProductList,
	@Nullable
	String nextCursor
) {
	public static InterestedSellProductResponse from(
		List<ProductWithFirstPhoto> productList,
		Map<Long, Boolean> interestMap,
		Map<Long, String> genreMap,
		Long currentStoreId,
		ProductPagination pagination,
		SortOption sortOption,
		Long lastInterestId,
		LocalDateTime lastInterestCreatedAt
	) {
		List<ProductSellDto> dtoList = productList.stream()
			.map(product -> {
				String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt());
				boolean isInterested = interestMap.getOrDefault(product.getId(), false);
				String genreName = genreMap.getOrDefault(product.getGenreId(), "기타");
				boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId());

				return ProductSellDto.from(
					product, uploadTime, isInterested, genreName, isOwnedByCurrentUser
				);
			})
			.toList();

		String nextCursor = pagination.generateNextInterestCursor(sortOption, lastInterestId, lastInterestCreatedAt);

		return new InterestedSellProductResponse(dtoList, nextCursor);
	}
}
