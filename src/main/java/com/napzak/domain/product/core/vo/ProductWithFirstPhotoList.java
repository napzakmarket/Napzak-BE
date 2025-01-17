package com.napzak.domain.product.core.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class ProductWithFirstPhotoList {
	private final List<ProductWithFirstPhoto> productList;

	public ProductWithFirstPhotoList(List<ProductWithFirstPhoto> productList) {
		this.productList = Objects.requireNonNullElseGet(productList, ArrayList::new);
	}

	@Nullable
	public ProductWithFirstPhoto first() {
		return productList.stream()
			.findFirst()
			.orElseGet(() -> null);
	}

	@Nullable
	public ProductWithFirstPhoto last() {
		if (productList.isEmpty()) {
			return null;
		}

		final int lastIndex = productList.size() - 1;
		return productList.get(lastIndex);
	}

	public int size() {
		return productList.size();
	}

	public boolean isEmpty() {
		return productList.isEmpty();
	}

	public List<ProductWithFirstPhoto> getProductList() {
		return productList;
	}
}
