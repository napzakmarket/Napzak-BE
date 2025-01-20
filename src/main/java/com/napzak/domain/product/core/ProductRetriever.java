package com.napzak.domain.product.core;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRetriever {

	private final ProductRepository productRepository;

	public Product findById(Long id) {
		ProductEntity productEntity = productRepository.findById(id)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		return Product.fromEntity(productEntity);
	}

	public List<Product> retrieveProducts(
		SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {

		return productRepository.findProductsBySortOptionAndFilters(
				sortOption.toOrderSpecifier(), cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, tradeType
			).stream()
			.map(Product::fromEntity)
			.toList();
	}

	public List<Product> retrieveStoreProducts(
		Long storeId, SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {

		return productRepository.findProductsByStoreIdAndSortOptionAndFilters(
				storeId, sortOption.toOrderSpecifier(), cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, tradeType
			).stream()
			.map(Product::fromEntity)
			.toList();
	}

	public List<Product> searchProducts(
		String searchWord, SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {

		return productRepository.searchProductsBySearchWordAndSortOptionAndFilters(
				searchWord, sortOption.toOrderSpecifier(), cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, tradeType
			).stream()
			.map(Product::fromEntity)
			.toList();
	}
}
