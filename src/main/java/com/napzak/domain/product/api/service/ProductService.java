package com.napzak.domain.product.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.domain.product.core.ProductPhotoRetriever;
import com.napzak.domain.product.core.ProductPhotoSaver;
import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.domain.product.core.ProductSaver;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.domain.product.core.vo.ProductPhoto;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;
import com.napzak.domain.product.core.vo.ProductWithFirstPhotoList;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRetriever productRetriever;
	private final ProductPhotoRetriever productPhotoRetriever;
	private final ProductSaver productSaver;
	private final ProductPhotoSaver productPhotoSaver;

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

	@Transactional
	public Product createSellProduct(
		String title, Long storeId, String description,
		int price, Boolean isDeliveryIncluded, int standardDeliveryFee, int halfDeliveryFee,
		ProductCondition productCondition, Long genreId
	) {
		Product product = productSaver.save(
			title, storeId, description, TradeType.SELL, TradeStatus.BEFORE_TRADE, price,
			false, isDeliveryIncluded, standardDeliveryFee,
			halfDeliveryFee, productCondition, genreId
		);

		return product;
	}

	@Transactional
	public Product createBuyProduct(
		String title, Long storeId, String description,
		int price, Boolean isPriceNegotiable, Long genreId
	) {
		Product product = productSaver.save(
			title, storeId, description, TradeType.BUY, TradeStatus.BEFORE_TRADE, price,
			isPriceNegotiable, false, 0,
			0, null, genreId
		);

		return product;
	}

	@Transactional
	public List<ProductPhoto> createProductPhotos(Long productId, Map<Integer, String> photoData) {

		return productPhotoSaver.saveAll(productId, photoData);
	}

	public Product getProduct(Long productId) {

		return productRetriever.findById(productId);
	}

	public List<ProductPhoto> getProductPhotos(Long productId) {

		return productPhotoRetriever.getProductPhotosByProductId(productId);
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

	public ProductPagination searchRecommendBuyProducts(Long storeId, List<Long> genreIds) {

		int size = 2;

		return retrieveAndPreparePagination(
			() -> productRetriever.getRecommendedBuyProducts(
				storeId, genreIds
			),
			size
		);
	}

	public ProductPagination searchRecommendSellProducts(Long storeId, List<Long> genreIds) {

		int size = 2;

		return retrieveAndPreparePagination(
			() -> productRetriever.getRecommendedSellProducts(
				storeId, genreIds
			),
			size
		);
	}

	public ProductWithFirstPhoto getProductChatInfo(Long productId) {
		Product product = productRetriever.findById(productId);
		String firstPhoto = productPhotoRetriever.getFirstProductPhoto(productId);

		return ProductWithFirstPhoto.from(product, firstPhoto);

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

	@FunctionalInterface
	private interface ProductRetrieval {
		List<Product> retrieve();
	}
}