package com.napzak.domain.product.core;

import static com.napzak.domain.product.core.entity.QProductEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProductEntity> findProductsBySortOptionAndFilters(
		OrderSpecifier<?> orderSpecifier, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {

		return queryFactory.selectFrom(productEntity)
			.where(
				genreFilter(genreIds),
				isUnopenedFilter(isUnopened),
				tradeTypeFilter(tradeType),
				isOnSaleFilter(isOnSale),
				cursorFilter(cursorProductId, cursorOptionalValue, orderSpecifier)
			)
			.orderBy(orderSpecifier)
			.limit(size + 1)
			.fetch();
	}

	@Override
	public List<ProductEntity> searchProductsBySearchWordAndSortOptionAndFilters(
		String searchWord, OrderSpecifier<?> orderSpecifier, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {
		return queryFactory.selectFrom(productEntity)
			.where(
				matchProductTitle(searchWord).gt(0), // FullText match 결과가 0보다 큰 row만
				genreFilter(genreIds),
				isUnopenedFilter(isUnopened),
				tradeTypeFilter(tradeType),
				isOnSaleFilter(isOnSale),
				cursorFilter(cursorProductId, cursorOptionalValue, orderSpecifier)
			)
			.orderBy(orderSpecifier)
			.limit(size + 1)
			.fetch();
	};

	@Override
	public List<ProductEntity> findProductsByStoreIdAndSortOptionAndFilters(
		Long storeId, OrderSpecifier<?> orderSpecifier, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {
		return queryFactory.selectFrom(productEntity)
			.where(
				productEntity.storeId.eq(storeId),
				genreFilter(genreIds),
				isUnopenedFilter(isUnopened),
				tradeTypeFilter(tradeType),
				isOnSaleFilter(isOnSale),
				cursorFilter(cursorProductId, cursorOptionalValue, orderSpecifier)
			)
			.orderBy(orderSpecifier)
			.limit(size + 1)
			.fetch();
	}

    @Override
    public List<ProductEntity> findProductsBySortOptionExcludingStoreId( // 네이밍 storeId 제외한다는 거 반영해서 바꾸기
                                                                         OrderSpecifier<?> orderSpecifier, int size, TradeType tradeType, long storeId) {

        return queryFactory.selectFrom(productEntity)
                .where(
                        tradeTypeFilter(tradeType),
                        productEntity.storeId.ne(storeId)
                )
                .orderBy(orderSpecifier)
                .limit(size)
                .fetch();
    }

	private BooleanExpression tradeTypeFilter(TradeType tradeType) {
		if (tradeType != null) {
			return productEntity.tradeType.eq(tradeType);
		}
		return null;
	}

	private BooleanExpression isOnSaleFilter(Boolean isOnSale) {
		if (isOnSale != null && isOnSale) {
			return productEntity.tradeStatus.eq(TradeStatus.BEFORE_TRADE);
		}
		return null;
	}

	private BooleanExpression isUnopenedFilter(Boolean isUnopened) {
		if (isUnopened != null && isUnopened) {
			return productEntity.productCondition.eq(ProductCondition.NEW);
		}
		return null;
	}

	private BooleanExpression genreFilter(List<Long> genreIds) {
		if (genreIds != null && !genreIds.isEmpty()) {
			return productEntity.genreId.in(genreIds);
		}
		return null;
	}

	private BooleanExpression cursorFilter(Long cursorProductId, Integer cursorOptionalValue,
		OrderSpecifier<?> orderSpecifier) {
		if (cursorProductId == null) {
			return null;
		}

		String fieldName = ((PathBuilder<?>)orderSpecifier.getTarget()).getMetadata().getName();
		com.querydsl.core.types.Order direction = orderSpecifier.getOrder();

		if (fieldName.equals("id")) {
			return direction == com.querydsl.core.types.Order.ASC
				? productEntity.id.goe(cursorProductId)
				: productEntity.id.loe(cursorProductId); // RECENT
		} else if (fieldName.equals("interestCount")) {
			return productEntity.interestCount.loe(cursorOptionalValue)
				.or(productEntity.interestCount.eq(cursorOptionalValue)
					.and(productEntity.id.loe(cursorProductId))); // POPULAR
		} else if (fieldName.equals("price")) {
			return direction == com.querydsl.core.types.Order.ASC
				? productEntity.price.goe(cursorOptionalValue) // LOW_PRICE
				: productEntity.price.loe(cursorOptionalValue); // HIGH_PRICE
		}

		return null;
	}

	private NumberTemplate<Double> matchProductTitle(String searchWord) {
		if (searchWord == null || searchWord.isBlank()) {
			return Expressions.numberTemplate(Double.class, "0");
		}

		// 검색어 파싱 및 Boolean Query 생성
		String[] tokens = searchWord.split("\\s+"); // 공백으로 단어 분리
		String booleanQuery = "+" + String.join(" +", tokens); // 각 단어에 + 추가

		log.info("Generated Boolean Query for searchWord '{}': {}", searchWord, booleanQuery);

		return Expressions.numberTemplate(
			Double.class,
			"function('match', {0}, {1})",
			productEntity.title,
			booleanQuery);
	}
}
