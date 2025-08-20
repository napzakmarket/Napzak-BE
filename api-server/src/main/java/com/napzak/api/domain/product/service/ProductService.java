package com.napzak.api.domain.product.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.domain.product.ProductChatFacade;
import com.napzak.domain.product.crud.productphoto.ProductPhotoRemover;
import com.napzak.domain.product.crud.productphoto.ProductPhotoRetriever;
import com.napzak.domain.product.crud.productphoto.ProductPhotoSaver;
import com.napzak.domain.product.crud.productphoto.ProductPhotoUpdater;
import com.napzak.domain.product.crud.product.ProductRemover;
import com.napzak.domain.product.crud.productreport.ProductReportSaver;
import com.napzak.domain.product.crud.product.ProductRetriever;
import com.napzak.domain.product.crud.product.ProductSaver;
import com.napzak.domain.product.crud.product.ProductUpdater;
import com.napzak.domain.product.crud.searchword.SearchWordRetriever;
import com.napzak.domain.product.entity.enums.ProductCondition;
import com.napzak.domain.product.entity.enums.TradeStatus;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.vo.Product;
import com.napzak.domain.product.vo.ProductPhoto;
import com.napzak.domain.product.vo.ProductWithFirstPhoto;
import com.napzak.domain.product.vo.ProductWithFirstPhotoList;
import com.napzak.domain.product.vo.SearchWord;
import com.napzak.api.domain.product.ProductInterestFacade;
import com.napzak.api.domain.product.dto.request.ProductPhotoModifyDto;
import com.napzak.api.domain.product.dto.response.RecommendSearchWordDto;
import com.napzak.domain.product.entity.enums.ProductSortOption;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRetriever productRetriever;
	private final ProductPhotoRetriever productPhotoRetriever;
	private final ProductSaver productSaver;
	private final ProductPhotoSaver productPhotoSaver;
	private final ProductPhotoRemover productPhotoRemover;
	private final ProductUpdater productUpdater;
	private final ProductPhotoUpdater productPhotoUpdater;
	private final SearchWordRetriever searchWordRetriever;
	private final ProductReportSaver productReportSaver;
	private final ProductInterestFacade productInterestFacade;
	private final ProductChatFacade productChatFacade;

	public ProductPagination getSellProducts(
		ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveProducts(
				productSortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, TradeType.SELL
			),
			size
		);
	}

	public ProductPagination getBuyProducts(
		ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveProducts(
				productSortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, null, genreIds, TradeType.BUY
			),
			size
		);
	}

	@Transactional(readOnly = true)
	public ProductPagination getInterestedProducts(
		Map<Long, Long> interestIdToProductIdMap, Long cursorInterestId, int size
	) {
		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveInterestedProducts(interestIdToProductIdMap, cursorInterestId, size),
			size
		);
	}

	public Set<Long> getTypeFilteredProductIdsByProductIds(List<Long> productIds, TradeType tradeType) {
		List<Product> productList = productRetriever.retrieveTypeFilteredproducts(productIds, tradeType);
		return productList.stream()
			.map(Product::getId)
			.collect(Collectors.toSet());
	}

	public ProductPagination searchSellProducts(
		String searchWord, ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, Boolean isUnopened, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.searchProducts(
				searchWord, productSortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, TradeType.SELL
			),
			size
		);
	}

	public ProductPagination searchBuyProducts(
		String searchWord, ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue, int size,
		Boolean isOnSale, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.searchProducts(
				searchWord, productSortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, null, genreIds, TradeType.BUY
			),
			size
		);
	}

	public ProductPagination getStoreSellProducts(
		Long storeId, ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, Boolean isUnopened, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveStoreProducts(
				storeId, productSortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, isUnopened, genreIds, TradeType.SELL
			),
			size
		);
	}

	public ProductPagination getStoreBuyProducts(
		Long storeId, ProductSortOption productSortOption, Long cursorProductId, Integer cursorOptionalValue,
		int size, Boolean isOnSale, List<Long> genreIds) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveStoreProducts(
				storeId, productSortOption, cursorProductId, cursorOptionalValue, size,
				isOnSale, null, genreIds, TradeType.BUY
			),
			size
		);
	}

	public int countProductsByFilters(Boolean isOnSale, Boolean isUnopened, List<Long> genreIds, TradeType tradeType) {
		return Math.toIntExact(productRetriever.countByFilters(isOnSale, isUnopened, genreIds, tradeType));
	}

	public int countProductsBySearchFilters(String searchWord, Boolean isOnSale, Boolean isUnopened,
		List<Long> genreIds, TradeType tradeType) {
		return Math.toIntExact(productRetriever.countBySearchWord(searchWord, isOnSale, isUnopened, genreIds, tradeType));
	}

	public int countProductsByStoreFilters(Long storeId, Boolean isOnSale, Boolean isUnopened,
		List<Long> genreIds, TradeType tradeType) {
		return Math.toIntExact(productRetriever.countByStore(storeId, isOnSale, isUnopened, genreIds, tradeType));
	}

	@Transactional
	public Product createSellProduct(
		String title, Long storeId, String description,
		int price, Boolean isDeliveryIncluded, int standardDeliveryFee, int halfDeliveryFee,
		ProductCondition productCondition, Long genreId
	) {
		Product product = productSaver.save(
			title, description, TradeType.SELL, TradeStatus.BEFORE_TRADE, price,
			false, isDeliveryIncluded, standardDeliveryFee,
			halfDeliveryFee, productCondition, storeId, genreId
		);

		return product;
	}

	@Transactional
	public Product updateSellProduct(
		Long productId, String title, String description,
		int price, Boolean isDeliveryIncluded, int standardDeliveryFee, int halfDeliveryFee,
		ProductCondition productCondition, Long genreId
	) {
		return productUpdater.updateProduct(
			productId, title, description, price, false, isDeliveryIncluded,
			standardDeliveryFee, halfDeliveryFee, productCondition, genreId
		);
	}

	@Transactional
	public Product createBuyProduct(
		String title, Long storeId, String description,
		int price, Boolean isPriceNegotiable, Long genreId
	) {
		Product product = productSaver.save(
			title, description, TradeType.BUY, TradeStatus.BEFORE_TRADE, price,
			isPriceNegotiable, false, 0,
			0, null, storeId, genreId
		);

		return product;
	}

	@Transactional
	public Product updateBuyProduct(
		Long productId, String title, String description,
		int price, Boolean isPriceNegotiable, Long genreId
	) {
		return productUpdater.updateProduct(
			productId, title, description, price, isPriceNegotiable, false,
			0, 0, null, genreId
		);
	}

	@Transactional
	public List<ProductPhoto> createProductPhotos(Long productId, Map<Integer, String> photoData) {
		return productPhotoSaver.saveAll(productId, photoData);
	}

	@Transactional(readOnly = true)
	public Product getProduct(Long productId) {
		return productRetriever.findById(productId);
	}

	@Transactional
	public void deleteProduct(Long productId) {
		productInterestFacade.deleteAllByProductId(productId);
		productUpdater.updateProductIsVisibleByProductId(productId, false);
		productChatFacade.updateChatMessageProductMetadataIsProductDeletedByProductId(productId, true);
	}

	@Transactional
	public void updateTradeStatus(Long productId, TradeStatus tradeStatus) {
		productUpdater.updateProductStatus(productId, tradeStatus);
	}

	@Transactional(readOnly = true)
	public List<ProductPhoto> getProductPhotos(Long productId) {
		return productPhotoRetriever.getProductPhotosByProductId(productId);
	}

	@Transactional(readOnly = true)
	public ProductPagination getHomePopularProducts(
		ProductSortOption productSortOption, int size, TradeType tradeType, Long storeId) {

		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveProductsExcludingCurrentUser(
				productSortOption, size, tradeType, storeId
			),
			size
		);
	}

	@Transactional(readOnly = true)
	public ProductPagination getHomeRecommendProducts(Long storeId, List<Long> genreIds) {
		return retrieveAndPreparePagination(
			() -> productRetriever.retrieveRecommendedProducts(storeId, genreIds),
			4
		);
	}

	@Transactional(readOnly = true)
	public ProductWithFirstPhoto getProductChatInfo(Long productId) {
		Product product = productRetriever.findByIdIncludingInvisible(productId);
		String firstPhoto = productPhotoRetriever.getFirstProductPhoto(productId);

		return ProductWithFirstPhoto.from(product, firstPhoto);
	}

	@Transactional
	public List<ProductPhoto> modifyProductPhotos(Long productId, List<ProductPhotoModifyDto> requestPhotos) {

		//기존 존재하는 사진들 조회
		List<ProductPhoto> existingPhotos = productPhotoRetriever.getProductPhotosByProductId(productId);

		//photoId 기준 객체 매핑
		Map<Long, ProductPhoto> existingPhotoMap = existingPhotos.stream()
			.filter(p -> p.getId() != null)
			.collect(Collectors.toMap(ProductPhoto::getId, p-> p));

		List<ProductPhoto> modifiedProductPhotoList = new ArrayList<>();
		Set<Long> requestPhotoIds = new HashSet<>();
		Map<Integer, String> newPhotos = new LinkedHashMap<>();

		for (ProductPhotoModifyDto photo : requestPhotos) {
			//요청으로 들어온 사진이 기존에 있던 사진이라면
			if (photo.photoId() != null && existingPhotoMap.containsKey(photo.photoId())) {
				requestPhotoIds.add(photo.photoId());
				ProductPhoto originalPhoto = existingPhotoMap.get(photo.photoId());
				//기존에 있던 사진과 요청으로 들어온 사진 간 순서나 url이 다른 경우 -> 수정 필요
				boolean needChange = !originalPhoto.getPhotoUrl().equals(photo.photoUrl()) || originalPhoto.getSequence() != photo.sequence();
				if (needChange) {
					productPhotoUpdater.updateProductPhoto(photo.photoId(), photo.photoUrl(), photo.sequence());
				}
			}
			else { //새로 등록이 필요한 사진이라면
				newPhotos.put(photo.sequence(), photo.photoUrl());
			}
		}

		List<ProductPhoto> addedPhotos = productPhotoSaver.saveAll(productId, newPhotos);
		modifiedProductPhotoList.addAll(addedPhotos);

		//요청에는 없지만 db에는 있는 사진 id를 찾아 객체 삭제
		List<Long> needRemovePhotoIds = existingPhotos.stream()
			.map(ProductPhoto::getId)
			.filter(id -> !requestPhotoIds.contains(id))
			.toList();
		productPhotoRemover.deleteAllByProductPhotoIds(needRemovePhotoIds);

		return productPhotoRetriever.getProductPhotosByProductId(productId);
	}

	@Transactional(readOnly = true)
	public List<RecommendSearchWordDto> getRecommendSearchWord() {
		List<SearchWord> recommendSearchWordList = searchWordRetriever.findAll();
		return recommendSearchWordList.stream()
			.map(searchWord -> RecommendSearchWordDto.from(searchWord.getId(), searchWord.getSearchWord()))
			.toList();
	}

	@Transactional
	public void reportProduct(Long reporterId, Product product, List<ProductPhoto> photoList, String title, String description, String contact) {
		productReportSaver.save(reporterId, product, photoList, title, description, contact);
	}

	public int countByStoreIdAndTradeTypeAndIsVisibleTrue(Long storeId, TradeType tradeType) {
		return productRetriever.countProductsByStoreIdAndTradeType(storeId, tradeType);
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