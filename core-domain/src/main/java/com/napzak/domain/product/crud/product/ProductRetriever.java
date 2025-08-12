package com.napzak.domain.product.crud.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.entity.enums.ProductSortOption;
import com.napzak.domain.product.entity.ProductEntity;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.repository.ProductRepository;
import com.napzak.domain.product.vo.Product;
import com.napzak.domain.product.code.ProductErrorCode;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRetriever {

	private final ProductRepository productRepository;
	private static final List<Long> FALLBACK_GENRES = List.of(1L, 7L, 4L, 5L);

	@Transactional(readOnly = true)
	public Product findById(Long id) {
		ProductEntity productEntity = productRepository.findById(id)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		return Product.fromEntity(productEntity);
	}

	@Transactional(readOnly = true)
	public Product findByIdIncludingInvisible(Long id) {
		ProductEntity productEntity = productRepository.findByIdIncludingInvisible(id)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		return Product.fromEntity(productEntity);
	}

	@Transactional(readOnly = true)
	public int countProductsByStoreIdAndTradeType(Long storeId, TradeType tradeType) {
		return productRepository.countByStoreIdAndTradeTypeAndIsVisibleTrue(storeId, tradeType);
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveProducts(ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {

		return productRepository.findProductsBySortOptionAndFilters(productSortOption.toOrderSpecifier(), cursorProductId,
				cursorOptionalValue, size, isOnSale, isUnopened, genreIds, tradeType)
			.stream()
			.map(Product::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveInterestedProducts(
		Map<Long, Long> interestIdToProductIdMap, Long cursorInterestId, int size
	) {
		return productRepository.findInterestedProducts(
			interestIdToProductIdMap, cursorInterestId, size
		).stream().map(Product::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveTypeFilteredproducts(List<Long> productIds, TradeType tradeType) {
		List<ProductEntity> productEntityList = productRepository.findByIdInAndTradeType(productIds, tradeType);
		return productEntityList.stream()
			.map(Product::fromEntity)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveProductsExcludingCurrentUser(ProductSortOption productSortOption, int size, TradeType tradeType,
		long storeId) {

		return productRepository.findProductsBySortOptionExcludingStoreId(productSortOption.toOrderSpecifier(), size,
			tradeType, storeId).stream().map(Product::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveStoreProducts(Long storeId, ProductSortOption productSortOption, Long cursorProductId,
		Integer cursorOptionalValue, int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds,
		TradeType tradeType) {

		return productRepository.findProductsByStoreIdAndSortOptionAndFilters(storeId, productSortOption.toOrderSpecifier(),
				cursorProductId, cursorOptionalValue, size, isOnSale, isUnopened, genreIds, tradeType)
			.stream()
			.map(Product::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Product> searchProducts(String searchWord, ProductSortOption productSortOption, Long cursorProductId,
		Integer cursorOptionalValue, int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds,
		TradeType tradeType) {

		return productRepository.searchProductsBySearchWordAndSortOptionAndFilters(searchWord,
			productSortOption.toOrderSpecifier(), cursorProductId, cursorOptionalValue, size, isOnSale, isUnopened, genreIds,
			tradeType).stream().map(Product::fromEntity).toList();
	}

	public long countBySearchWord(String searchWord, Boolean isOnSale, Boolean isUnopened,
		List<Long> genreIds, TradeType tradeType) {
		return productRepository.countProductsBySearchFilters(searchWord, isOnSale, isUnopened, genreIds, tradeType);
	}

	public long countByStore(Long storeId, Boolean isOnSale, Boolean isUnopened,
		List<Long> genreIds, TradeType tradeType) {
		return productRepository.countProductsByStoreFilters(storeId, isOnSale, isUnopened, genreIds, tradeType);
	}

	public long countByFilters(Boolean isOnSale, Boolean isUnopened,
		List<Long> genreIds, TradeType tradeType) {
		return productRepository.countProductsByFilters(isOnSale, isUnopened, genreIds, tradeType);
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveRecommendedProducts(Long storeId, List<Long> preferredGenres) {
		// 추천 상품 조회 흐름:
		// 1. 관심 장르에서 SELL/BUY 상품 수집 (1쿼리)
		// 2. 부족하면 fallback 장르에서 추가 수집 (1쿼리)
		// 3. 그래도 부족하면 최신 SELL 2개 + BUY 2개 수집 (2쿼리)
		// => 2SELL + 2BUY 조합을 항상 보장하며, 장르 다양성을 최우선으로 고려

		List<ProductEntity> candidates = new ArrayList<>();

		boolean preferredGenreSufficient = false;

		// [1] 선호 장르가 있을 경우만 조회 시도
		if (preferredGenres != null && !preferredGenres.isEmpty()) {
			List<ProductEntity> productsFromPreferredGenres = productRepository.findProductsByGenresAndTradeTypesExcludingStoreId(
				preferredGenres,
				List.of(TradeType.SELL, TradeType.BUY),
				storeId,
				preferredGenres.size() * 4
			);
			candidates.addAll(productsFromPreferredGenres);

			if (isEnoughForSellBuy(candidates, 2, 2)) {
				preferredGenreSufficient = true;
			}
		}

		// [2] fallback 장르 조회는 선호 장르가 없거나 부족할 때만 수행
		if (!preferredGenreSufficient) {
			List<ProductEntity> productsFromFallbackGenres = productRepository.findProductsByGenresAndTradeTypesExcludingStoreId(
				FALLBACK_GENRES,
				List.of(TradeType.SELL, TradeType.BUY),
				storeId,
				FALLBACK_GENRES.size() * 4
			);
			candidates = mergeWithoutDuplicates(candidates, productsFromFallbackGenres);

			if (isEnoughForSellBuy(candidates, 2, 2)) {
				log.info("[HomeRecommend] fallback까지만으로 추천 상품 조합 충족");
			}
		}

		// [3] 여전히 부족하면 최신 SELL 2개 + BUY 2개 보완
		int shortageSell = 2 - countTradeType(candidates, TradeType.SELL);
		int shortageBuy = 2 - countTradeType(candidates, TradeType.BUY);

		if (shortageSell > 0) {
			List<ProductEntity> latestSellProducts = productRepository.findLatestProductsExcludingStoreId(
				storeId, TradeType.SELL, 2
			);
			candidates = mergeWithoutDuplicates(candidates, latestSellProducts);
		}

		if (shortageBuy > 0) {
			List<ProductEntity> latestBuyProducts = productRepository.findLatestProductsExcludingStoreId(
				storeId, TradeType.BUY, 2
			);
			candidates = mergeWithoutDuplicates(candidates, latestBuyProducts);
		}

		// [4] 최적 조합 선택 (장르 중복 허용 포함)
		List<ProductEntity> bestFourProducts = pickBestFourAllowingGenreOverlap(candidates, 2, 2);
		if (bestFourProducts.isEmpty()) return List.of();

		// [5] 정렬 후 DTO 변환
		return convertToProductList(reorderSellBuy(bestFourProducts));
	}


	private List<ProductEntity> pickBestFourAllowingGenreOverlap(List<ProductEntity> productList, int needSell, int needBuy) {
		if (productList.size() < 4) return List.of();

		List<ProductEntity> best = new ArrayList<>();
		int maxDistinctGenres = -1;

		List<List<ProductEntity>> combinations = combinationsOfSize(productList, 4);
		for (List<ProductEntity> combo : combinations) {
			long sellCount = combo.stream().filter(p -> p.getTradeType() == TradeType.SELL).count();
			long buyCount = combo.stream().filter(p -> p.getTradeType() == TradeType.BUY).count();

			if (sellCount == needSell && buyCount == needBuy) {
				long distinctGenreCount = combo.stream().map(ProductEntity::getGenreId).distinct().count();
				if (distinctGenreCount > maxDistinctGenres) {
					maxDistinctGenres = (int) distinctGenreCount;
					best = combo;
				}
			}
		}

		// fallback: 장르 중복되더라도 조건 만족 조합 하나라도 리턴
		if (best.isEmpty()) {
			for (List<ProductEntity> combo : combinations) {
				long sellCount = combo.stream().filter(p -> p.getTradeType() == TradeType.SELL).count();
				long buyCount = combo.stream().filter(p -> p.getTradeType() == TradeType.BUY).count();
				if (sellCount == needSell && buyCount == needBuy) {
					return combo;
				}
			}
		}

		return best;
	}

	private boolean isEnoughForSellBuy(List<ProductEntity> list, int needSell, int needBuy) {
		return countTradeType(list, TradeType.SELL) >= needSell && countTradeType(list, TradeType.BUY) >= needBuy;
	}

	private int countTradeType(List<ProductEntity> list, TradeType type) {
		return (int) list.stream().filter(p -> p.getTradeType() == type).count();
	}

	private List<ProductEntity> mergeWithoutDuplicates(List<ProductEntity> base, List<ProductEntity> extra) {
		Set<Long> baseIds = base.stream().map(ProductEntity::getId).collect(Collectors.toSet());
		List<ProductEntity> merged = new ArrayList<>(base);
		for (ProductEntity e : extra) {
			if (!baseIds.contains(e.getId())) {
				merged.add(e);
			}
		}
		return merged;
	}

	private List<ProductEntity> reorderSellBuy(List<ProductEntity> four) {
		List<ProductEntity> sells = four.stream().filter(p -> p.getTradeType() == TradeType.SELL).toList();
		List<ProductEntity> buys = four.stream().filter(p -> p.getTradeType() == TradeType.BUY).toList();
		List<ProductEntity> res = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			if (i < sells.size()) res.add(sells.get(i));
			if (i < buys.size()) res.add(buys.get(i));
		}
		return res;
	}

	private List<Product> convertToProductList(List<ProductEntity> entities) {
		return entities.stream().map(Product::fromEntity).toList();
	}

	private List<List<ProductEntity>> combinationsOfSize(List<ProductEntity> list, int k) {
		List<List<ProductEntity>> result = new ArrayList<>();
		backtrack(list, 0, k, new ArrayList<>(), result);
		return result;
	}

	private void backtrack(List<ProductEntity> list, int idx, int k, List<ProductEntity> curr, List<List<ProductEntity>> res) {
		if (curr.size() == k) {
			res.add(new ArrayList<>(curr));
			return;
		}
		for (int i = idx; i < list.size(); i++) {
			curr.add(list.get(i));
			backtrack(list, i + 1, k, curr, res);
			curr.remove(curr.size() - 1);
		}
	}
}
