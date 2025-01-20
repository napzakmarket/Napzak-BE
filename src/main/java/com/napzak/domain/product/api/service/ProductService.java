package com.napzak.domain.product.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.domain.product.core.ProductPhotoRetriever;
import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;
import com.napzak.domain.product.core.vo.ProductWithFirstPhotoList;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRetriever productRetriever;
	private final ProductPhotoRetriever productPhotoRetriever;

	public ProductPagination getSellProducts(
		SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveProducts(
				sortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, TradeType.SELL
			),
			size
		);
	}

	public ProductPagination getBuyProducts(
		SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveProducts(
				sortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, null, genreIds, TradeType.BUY
			),
			size
		);
	}

	public ProductPagination searchSellProducts(
		String searchWord, SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.searchProducts(
				searchWord, sortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, TradeType.SELL
			),
			size
		);
	}

	public ProductPagination searchBuyProducts(
		String searchWord, SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.searchProducts(
				searchWord, sortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, null, genreIds, TradeType.BUY
			),
			size
		);
	}

	public ProductPagination getStoreSellProducts(
		Long storeId, SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveStoreProducts(
				storeId, sortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, TradeType.SELL
			),
			size
		);
	}

	public ProductPagination getStoreBuyProducts(
		Long storeId, SortOption sortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveStoreProducts(
				storeId, sortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, null, genreIds, TradeType.BUY
			),
			size
		);
	}

	public ProductPagination getHomePopularProducts(
		SortOption sortOption, int size, TradeType tradeType, Long storeId) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveProductsExcludingCurrentUser(
				sortOption, size, tradeType, storeId
			),
			size
		);
	}

	private ProductPagination retrieveAndPreparePagination(
		ProductRetrieval retrievalLogic,
		int size
	) {
		// 1. 상품 데이터 조회
		List<Product> products = retrievalLogic.retrieve();

		// 2. 상품 ID 리스트 추출
		List<Long> productIds = products.stream().map(Product::getId).toList();

		// 3. 첫 번째 사진 정보 조회
		Map<Long, String> firstPhotos = productPhotoRetriever.getFirstProductPhotos(productIds);

		// 4. 상품과 첫 번째 사진 결합
		List<ProductWithFirstPhoto> productWithFirstPhotoList = products.stream()
			.map(product -> ProductWithFirstPhoto.from(
				product,
				firstPhotos.getOrDefault(product.getId(), null)
			)).toList();

		// 5. ProductPagination 생성
		return new ProductPagination(size, new ProductWithFirstPhotoList(productWithFirstPhotoList));
	}

	// 맞춤형 - 구해요
	public ProductPagination searchRecommendBuyProducts(Long storeId, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.getRecommendedBuyProducts(
				storeId, genreIds
			),
			2
		);
	}

	// 맞춤형 - 팔아요
	public ProductPagination searchRecommendSellProducts(Long storeId, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.getRecommendedSellProducts(
				storeId, genreIds
			),
			2
		);
	}

	@FunctionalInterface
	private interface ProductRetrieval {
		List<Product> retrieve();
	}
}