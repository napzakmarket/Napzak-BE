package com.napzak.domain.product.core;

import java.util.List;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.querydsl.core.types.OrderSpecifier;

public interface ProductRepositoryCustom {
	List<ProductEntity> findProductsBySortOptionAndFilters(
		OrderSpecifier<?> orderSpecifier, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType);

	List<ProductEntity> searchProductsBySearchWordAndSortOptionAndFilters(
		String searchWord, OrderSpecifier<?> orderSpecifier, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType);

	List<ProductEntity> findProductsByStoreIdAndSortOptionAndFilters(
		Long storeId, OrderSpecifier<?> orderSpecifier, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType);
}
