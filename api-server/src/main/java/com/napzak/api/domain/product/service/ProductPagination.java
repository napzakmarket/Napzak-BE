package com.napzak.api.domain.product.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;

import com.napzak.api.domain.product.dto.request.cursor.InterestCursor;
import com.napzak.domain.product.vo.ProductWithFirstPhoto;
import com.napzak.domain.product.vo.ProductWithFirstPhotoList;
import com.napzak.api.domain.product.dto.request.cursor.HighPriceCursor;
import com.napzak.api.domain.product.dto.request.cursor.LowPriceCursor;
import com.napzak.api.domain.product.dto.request.cursor.PopularCursor;
import com.napzak.api.domain.product.dto.request.cursor.RecentCursor;
import com.napzak.domain.product.entity.enums.ProductSortOption;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;

public class ProductPagination {
	private final int needSize; // 요청된 size
	private final ProductWithFirstPhotoList productList; // 전체 리스트

	public ProductPagination(int needSize, ProductWithFirstPhotoList productList) {
		this.needSize = needSize;
		if (productList == null) {
			this.productList = new ProductWithFirstPhotoList(List.of()); // 빈 리스트 반환
		} else {
			this.productList = productList;
		}
	}

	// 다음 페이지 데이터가 있는지 여부 확인
	private boolean hasMoreData() {
		return productList.size() > needSize;
	}

	// 다음 페이지를 위한 Cursor 반환
	@Nullable
	public String generateNextCursor(ProductSortOption productSortOption) {
		if (!hasMoreData()) {
			return null; // 더 이상 데이터가 없으면 null 반환
		}
		ProductWithFirstPhoto lastProduct = productList.getProductList().get(needSize); // 11번째 상품
		switch (productSortOption) {
			case RECENT:
				return new RecentCursor(lastProduct.getId()).toString();
			case POPULAR:
				return new PopularCursor(lastProduct.getInterestCount(), lastProduct.getId()).toString();
			case LOW_PRICE:
				return new LowPriceCursor(lastProduct.getPrice(), lastProduct.getId()).toString();
			case HIGH_PRICE:
				return new HighPriceCursor(lastProduct.getPrice(), lastProduct.getId()).toString();
			default:
				throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
		}
	}

	@Nullable
	public String generateNextInterestCursor(ProductSortOption productSortOption, Long lastInterestId, LocalDateTime lastInterestCreatedAt) {
		if (lastInterestId == null || lastInterestCreatedAt == null) {
			return null;
		} else if (productSortOption == ProductSortOption.INTEREST) {
			return new InterestCursor(lastInterestId, lastInterestCreatedAt).toString();
		}
		throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
	}

	public Long getLastProductId() {
		if (!hasMoreData()) return null;
		return productList.getProductList().get(needSize).getId();
	}

	// 클라이언트에 반환할 데이터만 제공
	public List<ProductWithFirstPhoto> getProductList() {
		return productList.getProductList().subList(0, Math.min(needSize, productList.size()));
	}
}
