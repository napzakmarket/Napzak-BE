package com.napzak.domain.product.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRetriever {

	private final ProductRepository productRepository;
	private static final List<Long> FALLBACK_GENRES = List.of(1L, 7L, 4L, 5L);

	public boolean existsById(Long productId) {
		return productRepository.existsById(productId);
	}

	public Product findById(Long id) {
		ProductEntity productEntity = productRepository.findById(id)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		return Product.fromEntity(productEntity);
	}

	@Transactional(readOnly = true)
	public List<Product> retrieveProducts(SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {

		return productRepository.findProductsBySortOptionAndFilters(sortOption.toOrderSpecifier(), cursorProductId,
				cursorOptionalValue, size, isOnSale, isUnopened, genreIds, tradeType)
			.stream()
			.map(Product::fromEntity)
			.toList();
	}

	public List<Product> retrieveProductsExcludingCurrentUser(SortOption sortOption, int size, TradeType tradeType,
		long storeId) {

		return productRepository.findProductsBySortOptionExcludingStoreId(sortOption.toOrderSpecifier(), size,
			tradeType, storeId).stream().map(Product::fromEntity).toList();
	}

	public List<Product> retrieveStoreProducts(Long storeId, SortOption sortOption, Long cursorProductId,
		Integer cursorOptionalValue, int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds,
		TradeType tradeType) {

		return productRepository.findProductsByStoreIdAndSortOptionAndFilters(storeId, sortOption.toOrderSpecifier(),
				cursorProductId, cursorOptionalValue, size, isOnSale, isUnopened, genreIds, tradeType)
			.stream()
			.map(Product::fromEntity)
			.toList();
	}

	public List<Product> searchProducts(String searchWord, SortOption sortOption, Long cursorProductId,
		Integer cursorOptionalValue, int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds,
		TradeType tradeType) {

		return productRepository.searchProductsBySearchWordAndSortOptionAndFilters(searchWord,
			sortOption.toOrderSpecifier(), cursorProductId, cursorOptionalValue, size, isOnSale, isUnopened, genreIds,
			tradeType).stream().map(Product::fromEntity).toList();
	}

	/**
	 * 홈화면용 추천상품을 조회:
	 *  - 선호 장르(각각 SELL2, BUY2) 모으기
	 *  - 부족하면 fallback 장르, 최신 상품
	 *  - 최종 2SELL+2BUY=4개
	 *  - 장르를 최대한 다르게 구성(겹치지 않게)
	 */
	public List<Product> retrieveRecommendedProducts(Long storeId, List<Long> preferredGenres) {
		// 1) 우선 선호 장르에서 최대한 수집
		List<ProductEntity> collected = collectFromPreferredGenres(storeId, preferredGenres);

		// 2) 2SELL+2BUY가 부족하면 fallback 장르에서 추가 수집
		if (!isEnoughForSellBuy(collected, 2, 2)) {
			collected = fillShortageWithFallback(storeId, collected, 2, 2);
		}

		// 3) 여전히 부족하면 최신 상품
		if (!isEnoughForSellBuy(collected, 2, 2)) {
			collected = fillShortageWithLatest(storeId, collected, 2, 2);
		}

		// 4) 이제 collected 내에서 2SELL+2BUY=4개 조합을 모두 탐색,
		//    "서로 다른 장르를 최대화"하는 조합을 고른다.
		List<ProductEntity> best4 = pickBestFourMaxDistinctGenre(collected, 2, 2);

		// 5) 만약 정말로 2SELL+2BUY가 불가능하면(리턴이 빈 리스트),
		//    요구사항에 따라 반환값 변경 가능
		//    우선은 "빈 리스트" 반환
		if (best4.isEmpty()) {
			return List.of();
		}

		// 6) SELL-BUY-SELL-BUY 순으로 정렬
		best4 = reorderSellBuy(best4);

		// 7) 엔티티 -> DTO 변환
		return best4.stream().map(Product::fromEntity).toList();
	}

	// ============================================================
	// 1) 선호장르에서 수집: 장르마다 SELL 최대2, BUY 최대2
	// ============================================================
	private List<ProductEntity> collectFromPreferredGenres(Long storeId, List<Long> preferredGenres) {
		List<ProductEntity> all = new ArrayList<>();
		for (Long genreId : preferredGenres) {
			// SELL 최대 2
			List<ProductEntity> sells = productRepository.findProductsByGenreAndTradeTypeExcludingStoreId(
				genreId, storeId, TradeType.SELL, 2
			);
			// BUY 최대 2
			List<ProductEntity> buys = productRepository.findProductsByGenreAndTradeTypeExcludingStoreId(
				genreId, storeId, TradeType.BUY, 2
			);
			all.addAll(sells);
			all.addAll(buys);
		}
		return deduplicate(all);
	}

	// ============================================================
	// 2) fallback 장르에서 부족분 수집 (방법: 장르마다 SELL2, BUY2)
	// ============================================================
	private List<ProductEntity> fillShortageWithFallback(
		Long storeId,
		List<ProductEntity> collected,
		int needSell,
		int needBuy
	) {
		int shortageSell = needSell - countSell(collected);
		int shortageBuy = needBuy - countBuy(collected);

		// 이미 충족이면 그대로
		if (shortageSell <= 0 && shortageBuy <= 0) {
			return collected;
		}

		// fallback 장르 전부에서 SELL2 + BUY2
		List<ProductEntity> fallbackAll = new ArrayList<>();
		for (Long genreId : FALLBACK_GENRES) {
			List<ProductEntity> sells = productRepository.findProductsByGenreAndTradeTypeExcludingStoreId(
				genreId, storeId, TradeType.SELL, 2
			);
			List<ProductEntity> buys = productRepository.findProductsByGenreAndTradeTypeExcludingStoreId(
				genreId, storeId, TradeType.BUY, 2
			);
			fallbackAll.addAll(sells);
			fallbackAll.addAll(buys);
		}

		List<ProductEntity> merged = mergeWithoutDuplicates(collected, fallbackAll);
		return merged;
	}

	// ============================================================
	// 3) 최신 상품으로 부족분 보충 (SELL/BUY 각각 shortage만큼)
	//    - 여기서는 "모든 장르"를 최신순으로 보되,
	//      tradeType에 따라 shortage만큼
	// ============================================================
	private List<ProductEntity> fillShortageWithLatest(
		Long storeId,
		List<ProductEntity> collected,
		int needSell,
		int needBuy
	) {
		int shortageSell = needSell - countSell(collected);
		int shortageBuy = needBuy - countBuy(collected);

		if (shortageSell <= 0 && shortageBuy <= 0) {
			return collected;
		}

		// 최신 SELL
		List<ProductEntity> additionalSells = new ArrayList<>();
		if (shortageSell > 0) {
			// "최신 SELL"만 shortageSell개
			additionalSells = productRepository.findLatestProductsExcludingStoreId(
				storeId,
				TradeType.SELL,
				shortageSell
			);
		}

		// 최신 BUY
		List<ProductEntity> additionalBuys = new ArrayList<>();
		if (shortageBuy > 0) {
			additionalBuys = productRepository.findLatestProductsExcludingStoreId(
				storeId,
				TradeType.BUY,
				shortageBuy
			);
		}

		// 합치기
		List<ProductEntity> merged = mergeWithoutDuplicates(collected, additionalSells);
		merged = mergeWithoutDuplicates(merged, additionalBuys);
		return merged;
	}

	// ============================================================
	// 4) pickBestFourMaxDistinctGenre()
	//    - collected 내에서 2SELL+2BUY=4개를 만드는 모든 조합 탐색
	//    - "서로 다른 장르 수"가 최대가 되는 조합을 선택
	//    - 여러 개라면 임의로 하나 선택 (혹은 다른 우선순위가 있다면 추가)
	// ============================================================
	private List<ProductEntity> pickBestFourMaxDistinctGenre(
		List<ProductEntity> collected,
		int needSell,
		int needBuy
	) {
		// 우선 "2SELL+2BUY"를 만족하는 4개 부분집합을 전부 찾는다.
		// 그중에서 "장르 수"가 최대인 조합을 반환.

		if (collected.size() < 4) {
			return Collections.emptyList();
		}

		// 부분집합을 찾기 위해, 간단히 "nC4"를 전수조사(브루트포스)하자.
		// n이 20 이하 정도면 충분히 빠름.
		List<ProductEntity> best = new ArrayList<>();
		int maxDistinctGenres = -1;

		List<List<ProductEntity>> allCombinations = combinationsOfSize(collected, 4);
		for (List<ProductEntity> combo : allCombinations) {
			long sellCount = combo.stream().filter(p -> p.getTradeType() == TradeType.SELL).count();
			long buyCount = combo.stream().filter(p -> p.getTradeType() == TradeType.BUY).count();

			if (sellCount == needSell && buyCount == needBuy) {
				// 장르 중복 확인
				long distinctGenreCount = combo.stream()
					.map(ProductEntity::getGenreId)
					.distinct()
					.count();

				if (distinctGenreCount > maxDistinctGenres) {
					maxDistinctGenres = (int)distinctGenreCount;
					best = combo; // 이 조합을 채택
				}
			}
		}

		// 만약 아무 조합도 없으면 빈 리스트
		// 있으면 best 반환
		return best;
	}

	// ============================================================
	// 5) SELL-BUY-SELL-BUY 순서로 재정렬
	//    (이미 2SELL,2BUY라고 가정)
	// ============================================================
	private List<ProductEntity> reorderSellBuy(List<ProductEntity> four) {
		if (four.size() < 4)
			return four; // 혹은 그냥 그대로

		// SELL만 추출
		List<ProductEntity> sells = four.stream()
			.filter(p -> p.getTradeType() == TradeType.SELL)
			.collect(Collectors.toList());

		// BUY만 추출
		List<ProductEntity> buys = four.stream()
			.filter(p -> p.getTradeType() == TradeType.BUY)
			.collect(Collectors.toList());

		List<ProductEntity> result = new ArrayList<>();
		for (int i = 0; i < Math.max(sells.size(), buys.size()); i++) {
			if (i < sells.size())
				result.add(sells.get(i));
			if (i < buys.size())
				result.add(buys.get(i));
		}
		return result;
	}

	// ============================================================
	// [조합] nCk 구하기 (브루트포스)
	// ============================================================
	private List<List<ProductEntity>> combinationsOfSize(List<ProductEntity> list, int k) {
		List<List<ProductEntity>> result = new ArrayList<>();
		backtrack(list, 0, k, new ArrayList<>(), result);
		return result;
	}

	private void backtrack(
		List<ProductEntity> list,
		int startIndex,
		int k,
		List<ProductEntity> current,
		List<List<ProductEntity>> result
	) {
		if (current.size() == k) {
			result.add(new ArrayList<>(current));
			return;
		}
		for (int i = startIndex; i < list.size(); i++) {
			current.add(list.get(i));
			backtrack(list, i + 1, k, current, result);
			current.remove(current.size() - 1);
		}
	}

	// ============================================================
	// 기타 보조 메서드
	// ============================================================
	private boolean isEnoughForSellBuy(List<ProductEntity> list, int needSell, int needBuy) {
		return countSell(list) >= needSell && countBuy(list) >= needBuy;
	}

	private int countSell(List<ProductEntity> list) {
		return (int)list.stream().filter(p -> p.getTradeType() == TradeType.SELL).count();
	}

	private int countBuy(List<ProductEntity> list) {
		return (int)list.stream().filter(p -> p.getTradeType() == TradeType.BUY).count();
	}

	private List<ProductEntity> deduplicate(List<ProductEntity> list) {
		// ID 기준 중복 제거
		Map<Long, ProductEntity> map = new LinkedHashMap<>();
		for (ProductEntity p : list) {
			map.put(p.getId(), p);
		}
		return new ArrayList<>(map.values());
	}

	private List<ProductEntity> mergeWithoutDuplicates(List<ProductEntity> base, List<ProductEntity> extra) {
		// base + extra 중복 제거
		Set<Long> baseIds = base.stream().map(ProductEntity::getId).collect(Collectors.toSet());
		List<ProductEntity> merged = new ArrayList<>(base);
		for (ProductEntity e : extra) {
			if (!baseIds.contains(e.getId())) {
				merged.add(e);
			}
		}
		return merged;
	}
}
