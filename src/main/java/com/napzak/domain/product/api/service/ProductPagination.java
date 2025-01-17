package com.napzak.domain.product.api.service;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.domain.product.api.dto.request.cursor.HighPriceCursor;
import com.napzak.domain.product.api.dto.request.cursor.LowPriceCursor;
import com.napzak.domain.product.api.dto.request.cursor.PopularCursor;
import com.napzak.domain.product.api.dto.request.cursor.RecentCursor;
import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;
import com.napzak.domain.product.core.vo.ProductWithFirstPhotoList;
import com.napzak.global.common.exception.NapzakException;

public class ProductPagination {
	private final int needSize; // 요청된 size
	private final ProductWithFirstPhotoList productList; // 전체 리스트

	public ProductPagination(int needSize, ProductWithFirstPhotoList productList) {
		this.needSize = needSize;
		if (productList == null || productList.isEmpty()) {
			throw new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND);
		}
		this.productList = productList;
	}

	// 다음 페이지 데이터가 있는지 여부 확인
	private boolean hasMoreData() {
		return productList.size() > needSize;
	}

	// 다음 페이지를 위한 Cursor 반환
	@Nullable
	public String generateNextCursor(SortOption sortOption) {
		if (!hasMoreData()) {
			return null; // 더 이상 데이터가 없으면 null 반환
		}
		ProductWithFirstPhoto lastProduct = productList.getProductList().get(needSize); // 11번째 상품
		switch (sortOption) {
			case RECENT:
				return new RecentCursor(lastProduct.getId()).toString();
			case POPULAR:
				return new PopularCursor(lastProduct.getInterestCount(), lastProduct.getId()).toString();
			case LOW_PRICE:
				return new LowPriceCursor(lastProduct.getPrice(), lastProduct.getId()).toString();
			case HIGH_PRICE:
				return new HighPriceCursor(lastProduct.getPrice(), lastProduct.getId()).toString();
			default:
				throw new NapzakException(ProductErrorCode.INVALID_SORT_OPTION);
		}
	}

	// 클라이언트에 반환할 데이터만 제공
	public List<ProductWithFirstPhoto> getProductList() {
		return productList.getProductList().subList(0, Math.min(needSize, productList.size()));
	}
}
