package com.napzak.domain.product.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
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

	public boolean existsById(Long productId) {
		return productRepository.existsById(productId);
	}

	public Product findById(Long id) {
		ProductEntity productEntity = productRepository.findById(id)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND));
		return Product.fromEntity(productEntity);
	}

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

	//선호장르를 받아 선호장르 우선으로 buy product를 2개씩 구성. 부족하다면 default 장르에서 중복없이 가져오기.
	public List<Product> getRecommendedBuyProducts(Long storeId, List<Long> genreIds) {

		List<ProductEntity> products = getPreferenceProducts(genreIds, TradeType.BUY, storeId);

		return products.stream().map(Product::fromEntity).toList();
	}

	//선호장르를 받아 선호장르 우선으로 sell product를 2개씩 구성. 부족하다면 default 장르에서 중복없이 가져오기.
	public List<Product> getRecommendedSellProducts(Long storeId, List<Long> genreIds) {

		Collections.reverse(genreIds);

		List<ProductEntity> products = getPreferenceProducts(genreIds, TradeType.SELL, storeId);

		return products.stream().map(Product::fromEntity).toList();
	}

	public List<ProductEntity> getPreferenceProducts(List<Long> genreIds, TradeType tradeType, Long storeId) {

		//정렬 기준 : 최신순
		Sort createdSort = Sort.by(Sort.Order.desc("createdAt"));
		//정렬 기준 : 인기순
		Sort interestSort = Sort.by(Sort.Order.desc("interestCount"));

		final List<Long> DEFAULT_GENRE_IDS = new ArrayList<>(List.of(2L, 8L, 5L, 6L));
		Set<Long> addedProductIds = new HashSet<>(); // 추가된 상품의 ID를 추적하기 위한 Set

		log.info("default 장르: {}", DEFAULT_GENRE_IDS);
		log.info("선호 장르: {}", genreIds);

		List<ProductEntity> productEntityList = new ArrayList<>();
		int productCount = 0;

		//최대 4개 장르에서 가능한 대로 하나씩 가져옴.
		for (int i = 0; i < 4; i++) {
			for (Long g : genreIds) {
				if (productCount >= 2) {
					break;
				}

				List<ProductEntity> product = productRepository.findTopByGenre(g, tradeType, storeId, 1, createdSort);
				log.info("가져온 product: {}", product);

				for (ProductEntity productEntity : product) {
					if (!addedProductIds.contains(productEntity.getId())) {
						productEntityList.add(productEntity);
						addedProductIds.add(productEntity.getId());
						productCount++;
					}
					if (productCount >= 2) {
						break;
					}
				}
			}
			log.info("현재 productEntityList: {}", productEntityList);
			if (productCount >= 2) {
				break;
			}
		}

		if (productEntityList.size() > 2) {
			productEntityList.sort(Comparator.comparing(ProductEntity::getCreatedAt).reversed());
			productEntityList = productEntityList.subList(0, 2);
		}

		while (productCount < 2) {
			productEntityList.addAll(
				productRepository.findTopByGenre(DEFAULT_GENRE_IDS.get(0), tradeType, storeId, 1, interestSort));
			DEFAULT_GENRE_IDS.remove(0);
			productCount++;
		}

		log.info("최종 productEntityList: {}", productEntityList);
		return productEntityList;
	}
}
