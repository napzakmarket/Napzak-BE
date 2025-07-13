package com.napzak.api.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.api.domain.product.service.ProductPagination;
import com.napzak.domain.product.entity.enums.ProductSortOption;
import com.napzak.domain.product.vo.ProductWithFirstPhoto;
import com.napzak.common.util.TimeUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InterestedBuyProductResponse(
	List<ProductBuyDto> interestedBuyProductList,
	@Nullable
	String nextCursor
) {
	public static InterestedBuyProductResponse from(
		List<ProductWithFirstPhoto> productList,
		Map<Long, Boolean> interestMap,
		Map<Long, String> genreMap,
		Long currentStoreId,
		ProductPagination pagination,
		ProductSortOption productSortOption,
		Long lastInterestId,
		LocalDateTime lastInterestCreatedAt
	) {
		List<ProductBuyDto> dtoList = productList.stream()
			.map(product -> {
				String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt());
				boolean isInterested = interestMap.getOrDefault(product.getId(), false);
				String genreName = genreMap.getOrDefault(product.getGenreId(), "기타");
				boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId());

				return ProductBuyDto.from(
					product, uploadTime, isInterested, genreName, isOwnedByCurrentUser
				);
			})
			.toList();

		String nextCursor = pagination.generateNextInterestCursor(productSortOption, lastInterestId, lastInterestCreatedAt);

		return new InterestedBuyProductResponse(dtoList, nextCursor);
	}
}
