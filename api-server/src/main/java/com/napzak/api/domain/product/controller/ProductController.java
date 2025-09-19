package com.napzak.api.domain.product.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.domain.product.ProductChatFacade;
import com.napzak.api.domain.product.ProductGenreFacade;
import com.napzak.api.domain.product.dto.request.cursor.InterestCursor;
import com.napzak.api.domain.product.dto.response.InterestedBuyProductResponse;
import com.napzak.api.domain.product.dto.response.InterestedSellProductResponse;
import com.napzak.api.domain.product.code.ProductSuccessCode;
import com.napzak.api.domain.product.service.ProductPhotoS3ImageCleaner;
import com.napzak.api.domain.product.dto.response.ProductBuyListResponse;
import com.napzak.api.domain.product.dto.response.ProductBuyModifyResponse;
import com.napzak.api.domain.product.dto.response.ProductBuyResponse;
import com.napzak.api.domain.product.dto.response.ProductReportResponse;
import com.napzak.api.domain.product.dto.response.ProductSellResponse;
import com.napzak.api.domain.product.dto.response.RecommendSearchWordDto;
import com.napzak.api.domain.product.service.ProductPagination;
import com.napzak.api.domain.product.service.ProductService;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.vo.Product;
import com.napzak.domain.product.vo.ProductPhoto;
import com.napzak.domain.product.vo.ProductWithFirstPhoto;
import com.napzak.domain.genre.vo.Genre;
import com.napzak.domain.interest.vo.Interest;
import com.napzak.api.domain.product.ProductInterestFacade;
import com.napzak.api.domain.product.ProductStoreFacade;
import com.napzak.api.domain.product.dto.request.ProductBuyCreateRequest;
import com.napzak.api.domain.product.dto.request.ProductBuyModifyRequest;
import com.napzak.api.domain.product.dto.request.ProductPhotoRequestDto;
import com.napzak.api.domain.product.dto.request.ProductReportRequest;
import com.napzak.api.domain.product.dto.request.ProductSellCreateRequest;
import com.napzak.api.domain.product.dto.request.ProductSellModifyRequest;
import com.napzak.api.domain.product.dto.request.TradeStatusRequest;
import com.napzak.api.domain.product.dto.request.cursor.HighPriceCursor;
import com.napzak.api.domain.product.dto.request.cursor.LowPriceCursor;
import com.napzak.api.domain.product.dto.request.cursor.PopularCursor;
import com.napzak.api.domain.product.dto.request.cursor.RecentCursor;
import com.napzak.api.domain.product.dto.response.ProductChatInfoResponse;
import com.napzak.api.domain.product.dto.response.ProductDetailDto;
import com.napzak.api.domain.product.dto.response.ProductDetailResponse;
import com.napzak.api.domain.product.dto.response.ProductPhotoDto;
import com.napzak.api.domain.product.dto.response.ProductRecommendListResponse;
import com.napzak.api.domain.product.dto.response.ProductSellListResponse;
import com.napzak.api.domain.product.dto.response.ProductSellModifyResponse;
import com.napzak.api.domain.product.dto.response.RecommendGenreDto;
import com.napzak.api.domain.product.dto.response.RecommendResponse;
import com.napzak.domain.product.code.ProductErrorCode;
import com.napzak.domain.product.entity.enums.ProductSortOption;
import com.napzak.domain.store.vo.BlockStatus;
import com.napzak.domain.store.vo.Store;
import com.napzak.domain.store.vo.StoreStatus;
import com.napzak.common.auth.annotation.AuthorizedRole;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.common.util.TimeUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController implements ProductApi {
	private final ProductService productService;
	private final ProductChatFacade productChatFacade;
	private final ProductInterestFacade productInterestFacade;
	private final ProductGenreFacade productGenreFacade;
	private final ProductStoreFacade productStoreFacade;
	private final ProductPhotoS3ImageCleaner productPhotoS3ImageCleaner;

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/sell")
	public ResponseEntity<SuccessResponse<ProductSellListResponse>> getSellProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(defaultValue = "false") Boolean isUnopened,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	) {
		// 1. 요청 파라미터 파싱
		ProductSortOption parsedProductSortOption = parseSortOption(sortOption);
		CursorValues cursorValues = parseCursorValues(cursor, parsedProductSortOption);

		// 2. 상품 데이터 조회
		ProductPagination pagination = productService.getSellProducts(
			parsedProductSortOption,
			cursorValues.getCursorId(),
			cursorValues.getCursorOptionalValue(),
			size,
			isOnSale,
			isUnopened,
			genreId
		);
		int productCount = productService.countProductsByFilters(isOnSale, isUnopened, genreId, TradeType.SELL);

		// 3. 관심, 장르이름 정보 조회
		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, storeId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		// 4. 응답 생성
		ProductSellListResponse response = ProductSellListResponse.from(
			productCount, parsedProductSortOption, pagination, interestMap, genreMap, storeId
		);

		// 5. 응답 반환
		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_LIST_RETRIEVE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/buy")
	public ResponseEntity<SuccessResponse<ProductBuyListResponse>> getBuyProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	) {
		ProductSortOption parsedProductSortOption = parseSortOption(sortOption);
		CursorValues cursorValues = parseCursorValues(cursor, parsedProductSortOption);

		ProductPagination pagination = productService.getBuyProducts(
			parsedProductSortOption,
			cursorValues.getCursorId(),
			cursorValues.getCursorOptionalValue(),
			size,
			isOnSale,
			genreId
		);
		int productCount = productService.countProductsByFilters(
			isOnSale, false, genreId, TradeType.BUY
		);

		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, storeId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		ProductBuyListResponse response = ProductBuyListResponse.from(
			productCount, parsedProductSortOption, pagination, interestMap, genreMap, storeId
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_LIST_RETRIEVE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/sell/search")
	public ResponseEntity<SuccessResponse<ProductSellListResponse>> searchSellProducts(
		@RequestParam String searchWord,
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(defaultValue = "false") Boolean isUnopened,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	) {
		ProductSortOption parsedProductSortOption = parseSortOption(sortOption);
		CursorValues cursorValues = parseCursorValues(cursor, parsedProductSortOption);

		ProductPagination pagination = productService.searchSellProducts(
			searchWord,
			parsedProductSortOption,
			cursorValues.getCursorId(),
			cursorValues.getCursorOptionalValue(),
			size,
			isOnSale,
			isUnopened,
			genreId
		);
		int productCount = productService.countProductsBySearchFilters(
			searchWord, isOnSale, isUnopened, genreId, TradeType.SELL
		);

		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, storeId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		ProductSellListResponse response = ProductSellListResponse.from(
			productCount, parsedProductSortOption, pagination, interestMap, genreMap, storeId
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_LIST_SEARCH_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/buy/search")
	public ResponseEntity<SuccessResponse<ProductBuyListResponse>> searchBuyProducts(
		@RequestParam String searchWord,
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	) {
		ProductSortOption parsedProductSortOption = parseSortOption(sortOption);
		CursorValues cursorValues = parseCursorValues(cursor, parsedProductSortOption);

		ProductPagination pagination = productService.searchBuyProducts(
			searchWord,
			parsedProductSortOption,
			cursorValues.getCursorId(),
			cursorValues.getCursorOptionalValue(),
			size,
			isOnSale,
			genreId
		);
		int productCount = productService.countProductsBySearchFilters(
			searchWord, isOnSale, false, genreId, TradeType.BUY
		);

		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, storeId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		ProductBuyListResponse response = ProductBuyListResponse.from(
			productCount, parsedProductSortOption, pagination, interestMap, genreMap, storeId
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_LIST_RETRIEVE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("sell/stores/{storeOwnerId}")
	public ResponseEntity<SuccessResponse<ProductSellListResponse>> getStoreSellProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(defaultValue = "false") Boolean isUnopened,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@PathVariable Long storeOwnerId,
		@CurrentMember Long currentStoreId
	) {
		ProductSortOption parsedProductSortOption = parseSortOption(sortOption);
		CursorValues cursorValues = parseCursorValues(cursor, parsedProductSortOption);

		ProductPagination pagination = productService.getStoreSellProducts(
			storeOwnerId,
			parsedProductSortOption,
			cursorValues.getCursorId(),
			cursorValues.getCursorOptionalValue(),
			size,
			isOnSale,
			isUnopened,
			genreId
		);
		int productCount = productService.countProductsByStoreFilters(
			storeOwnerId, isOnSale, isUnopened, genreId, TradeType.SELL
		);

		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		ProductSellListResponse response = ProductSellListResponse.from(
			productCount, parsedProductSortOption, pagination, interestMap, genreMap, currentStoreId
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_LIST_RETRIEVE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/buy/stores/{storeOwnerId}")
	public ResponseEntity<SuccessResponse<ProductBuyListResponse>> getStoreBuyProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@PathVariable Long storeOwnerId,
		@CurrentMember Long currentStoreId
	) {
		ProductSortOption parsedProductSortOption = parseSortOption(sortOption);
		CursorValues cursorValues = parseCursorValues(cursor, parsedProductSortOption);

		ProductPagination pagination = productService.getStoreBuyProducts(
			storeOwnerId,
			parsedProductSortOption,
			cursorValues.getCursorId(),
			cursorValues.getCursorOptionalValue(),
			size,
			isOnSale,
			genreId
		);
		int productCount = productService.countProductsByStoreFilters(
			storeOwnerId, isOnSale, false, genreId, TradeType.BUY
		);

		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		ProductBuyListResponse response = ProductBuyListResponse.from(
			productCount, parsedProductSortOption, pagination, interestMap, genreMap, currentStoreId
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_LIST_RETRIEVE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PostMapping("/sell")
	public ResponseEntity<SuccessResponse<ProductSellResponse>> createSellProduct(
		@Valid @RequestBody ProductSellCreateRequest productSellCreateRequest,
		@CurrentMember Long storeId
	) {
		Product product = productService.createSellProduct(
			productSellCreateRequest.title(),
			storeId,
			productSellCreateRequest.description(),
			productSellCreateRequest.price(),
			productSellCreateRequest.isDeliveryIncluded(),
			productSellCreateRequest.standardDeliveryFee(),
			productSellCreateRequest.halfDeliveryFee(),
			productSellCreateRequest.productCondition(),
			productSellCreateRequest.genreId()
		);

		Map<Integer, String> photoData = productSellCreateRequest.productPhotoList().stream()
			.collect(Collectors.toMap(
				ProductPhotoRequestDto::sequence,
				ProductPhotoRequestDto::photoUrl,
				(existing, replacement) -> existing,  // 중복 키가 발생하면 기존 값 유지
				LinkedHashMap::new  // 순서 유지
			));

		List<ProductPhoto> productPhotoList = productService.createProductPhotos(product.getId(), photoData);

		ProductSellResponse response = ProductSellResponse.from(product, productPhotoList);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_CREATE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PostMapping("/buy")
	public ResponseEntity<SuccessResponse<ProductBuyResponse>> createBuyProduct(
		@Valid @RequestBody ProductBuyCreateRequest productBuyCreateRequest,
		@CurrentMember Long storeId
	) {
		Product product = productService.createBuyProduct(
			productBuyCreateRequest.title(),
			storeId,
			productBuyCreateRequest.description(),
			productBuyCreateRequest.price(),
			productBuyCreateRequest.isPriceNegotiable(),
			productBuyCreateRequest.genreId()
		);

		Map<Integer, String> photoData = productBuyCreateRequest.productPhotoList().stream()
			.collect(Collectors.toMap(
				ProductPhotoRequestDto::sequence,
				ProductPhotoRequestDto::photoUrl,
				(existing, replacement) -> existing,
				LinkedHashMap::new
			));

		List<ProductPhoto> productPhotoList = productService.createProductPhotos(product.getId(), photoData);

		ProductBuyResponse response = ProductBuyResponse.from(product, productPhotoList);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_CREATE_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/{productId}")
	public ResponseEntity<SuccessResponse<ProductDetailResponse>> getDetailProductInfo(
		@PathVariable("productId") Long productId,
		@CurrentMember final Long currentStoreId
	) {

		Product product = productService.getProduct(productId);

		boolean isInterested = productInterestFacade.getIsInterested(productId, currentStoreId); //좋아요 여부
		String uploadTime = TimeUtils.calculateUploadTime(product.getCreatedAt()); //업로드 시간
		String genreName = productGenreFacade.getGenreName(product.getGenreId()); //장르 이름
		boolean isOwnedByCurrentUser = currentStoreId.equals(product.getStoreId()); //자신의 상품 여부

		//각각의 dto 생성
		ProductDetailDto productDetailDto = ProductDetailDto.from(product, uploadTime, genreName,
			isOwnedByCurrentUser);

		List<ProductPhotoDto> photoDtoList = productService.getProductPhotos(productId).stream()
			.map(ProductPhotoDto::from)
			.toList();

		Store productOwner = productStoreFacade.getStoreById(product.getStoreId());
		int productOwnerSellCount = productService.countByStoreIdAndTradeTypeAndIsVisibleTrue(productOwner.getId(),
			TradeType.SELL);
		int productOwnerBuyCount = productService.countByStoreIdAndTradeTypeAndIsVisibleTrue(productOwner.getId(),
			TradeType.BUY);

		StoreStatus storeStatus = StoreStatus.from(
			productOwner.getId(),
			productOwner.getPhoto(),
			productOwner.getNickname(),
			productOwnerSellCount,
			productOwnerBuyCount
		);

		// List<Review> reviewList = productReviewFacade.findAllByStoreId(product.getStoreId());
		//
		// List<Long> reviewIds = reviewList.stream()
		// 	.map(Review::getId)
		// 	.toList();
		//
		// Map<Long, String> reviewerNicknames = productReviewFacade.findReviewerNamesByReviewId(product.getStoreId(),
		// 	reviewIds);
		//
		// List<StoreReviewDto> storeReviewDtoList = reviewList.stream()
		// 	.map(review -> {
		// 		String reviewerNickname = reviewerNicknames.get(review.getId());
		// 		return StoreReviewDto.from(review, reviewerNickname, product);
		// 	})
		// 	.toList();

		ProductDetailResponse response = new ProductDetailResponse(isInterested, productDetailDto, photoDtoList,
			storeStatus);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_DETAIL_GET_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PatchMapping("/{productId}")
	public ResponseEntity<SuccessResponse<Void>> updateTradeStatus(
		@CurrentMember Long currentStoreId,
		@PathVariable Long productId,
		@RequestBody TradeStatusRequest tradeStatusRequest
	) {

		Product product = productService.getProduct(productId);
		authChecker(currentStoreId, product);

		productService.updateTradeStatus(productId, tradeStatusRequest.tradeStatus());

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.PRODUCT_UPDATE_SUCCESS));
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@DeleteMapping("{productId}")
	public ResponseEntity<SuccessResponse<Void>> deleteProduct(
		@CurrentMember Long currentStoreId,
		@PathVariable Long productId
	) {

		Product product = productService.getProduct(productId);
		authChecker(currentStoreId, product);

		productService.deleteProduct(productId);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.PRODUCT_DELETE_SUCCESS));
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/sell/modify/{productId}")
	public ResponseEntity<SuccessResponse<ProductSellModifyResponse>> getSellProductForModify(
		@CurrentMember Long currentStoreId,
		@PathVariable Long productId
	) {

		Product product = productService.getProduct(productId);
		authChecker(currentStoreId, product);

		String genreName = productGenreFacade.getGenreName(product.getGenreId());

		List<ProductPhotoDto> photoDtoList = productService.getProductPhotos(productId).stream()
			.map(ProductPhotoDto::from)
			.toList();

		ProductSellModifyResponse productSellModifyresponse = ProductSellModifyResponse.from(
			product.getId(), product.getGenreId(), genreName, product.getTitle(), product.getDescription(),
			product.getProductCondition(), product.getPrice(), product.getIsDeliveryIncluded(),
			product.getStandardDeliveryFee(), product.getHalfDeliveryFee(), photoDtoList
		);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.PRODUCT_RETRIEVE_SUCCESS, productSellModifyresponse));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PutMapping("/sell/modify/{productId}")
	public ResponseEntity<SuccessResponse<ProductSellResponse>> modifySellProduct(
		@CurrentMember Long currentStoreId,
		@PathVariable Long productId,
		@Valid @RequestBody ProductSellModifyRequest productSellModifyRequest
	) {
		Product product = productService.getProduct(productId);
		authChecker(currentStoreId, product);

		product = productService.updateSellProduct(
			productId,
			productSellModifyRequest.title(),
			productSellModifyRequest.description(),
			productSellModifyRequest.price(),
			productSellModifyRequest.isDeliveryIncluded(),
			productSellModifyRequest.standardDeliveryFee(),
			productSellModifyRequest.halfDeliveryFee(),
			productSellModifyRequest.productCondition(),
			productSellModifyRequest.genreId()
		);

		List<ProductPhoto> productPhotoList = productService.modifyProductPhotos(product.getId(),
			productSellModifyRequest.productPhotoList());

		ProductSellResponse response = ProductSellResponse.from(product, productPhotoList);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_MODIFY_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/buy/modify/{productId}")
	public ResponseEntity<SuccessResponse<ProductBuyModifyResponse>> getBuyProductForModify(
		@CurrentMember Long currentStoreId,
		@PathVariable Long productId
	) {
		Product product = productService.getProduct(productId);
		authChecker(currentStoreId, product);

		String genreName = productGenreFacade.getGenreName(product.getGenreId());

		List<ProductPhotoDto> photoDtoList = productService.getProductPhotos(productId).stream()
			.map(ProductPhotoDto::from)
			.toList();

		ProductBuyModifyResponse productBuyModifyResponse = ProductBuyModifyResponse.from(
			product.getId(), product.getGenreId(), genreName, product.getTitle(), product.getDescription(),
			product.getPrice(),
			product.getIsPriceNegotiable(), photoDtoList
		);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.PRODUCT_RETRIEVE_SUCCESS, productBuyModifyResponse));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PutMapping("/buy/modify/{productId}")
	public ResponseEntity<SuccessResponse<ProductBuyResponse>> modifyBuyProduct(
		@CurrentMember Long currentStoreId,
		@PathVariable Long productId,
		@Valid @RequestBody ProductBuyModifyRequest productBuyModifyRequest
	) {
		Product product = productService.getProduct(productId);
		authChecker(currentStoreId, product);

		product = productService.updateBuyProduct(
			productId,
			productBuyModifyRequest.title(),
			productBuyModifyRequest.description(),
			productBuyModifyRequest.price(),
			productBuyModifyRequest.isPriceNegotiable(),
			productBuyModifyRequest.genreId()
		);

		List<ProductPhoto> productPhotoList = productService.modifyProductPhotos(product.getId(),
			productBuyModifyRequest.productPhotoList());

		ProductBuyResponse response = ProductBuyResponse.from(product, productPhotoList);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_MODIFY_SUCCESS,
				response
			)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/home/recommend")
	public ResponseEntity<SuccessResponse<ProductRecommendListResponse>> getRecommendProducts(
		@CurrentMember Long currentStoreId
	) {
		String nickname = productStoreFacade.getStoreNickname(currentStoreId);
		List<Long> genreIds = productStoreFacade.getGenrePreferenceIds(currentStoreId);

		ProductPagination pagination = productService.getHomeRecommendProducts(currentStoreId, genreIds);

		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		ProductRecommendListResponse productListResponse = ProductRecommendListResponse.from(nickname, pagination,
			interestMap,
			genreMap, currentStoreId);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.RECOMMEND_PRODUCT_GET_SUCCESS, productListResponse));
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/home/sell")
	public ResponseEntity<SuccessResponse<ProductSellListResponse>> getTopSellProducts(
		@RequestParam(defaultValue = "6") int size,
		@CurrentMember Long currentStoreId
	) {

		ProductSortOption parsedProductSortOption = ProductSortOption.POPULAR;

		ProductPagination pagination = productService.getHomePopularProducts(
			parsedProductSortOption, size, TradeType.SELL, currentStoreId);

		// 3. 관심, 장르이름 정보 조회
		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		// 4. 응답 생성
		ProductSellListResponse response = ProductSellListResponse.from(
			null, parsedProductSortOption, pagination, interestMap, genreMap, currentStoreId
		);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.TOP_SELL_PRODUCT_GET_SUCCESS, response));
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/home/buy")
	public ResponseEntity<SuccessResponse<ProductBuyListResponse>> getTopBuyProducts(
		@RequestParam(defaultValue = "6") int size,
		@CurrentMember Long currentStoreId
	) {

		ProductSortOption parsedProductSortOption = ProductSortOption.POPULAR;

		ProductPagination pagination = productService.getHomePopularProducts(
			parsedProductSortOption, size, TradeType.BUY, currentStoreId);

		// 3. 관심, 장르이름 정보 조회
		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);
		Map<Long, String> genreMap = fetchGenreMap(pagination);

		// 4. 응답 생성
		ProductBuyListResponse response = ProductBuyListResponse.from(
			null, parsedProductSortOption, pagination, interestMap, genreMap, currentStoreId
		);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.TOP_BUY_PRODUCT_GET_SUCCESS, response));
	}

	// 채팅방 목록에서 진입하거나 알림 통해서 진입할 때 자신의 상품에 대해 요청할 경우, RequestParam 으로 roomId 받아서 그대로 반환
	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/chat/{productId}")
	public ResponseEntity<SuccessResponse<ProductChatInfoResponse>> getProductChatInfo(
		@PathVariable Long productId,
		@RequestParam(required = false) Long roomId,
		@CurrentMember Long currentStoreId
	) {
		ProductWithFirstPhoto product = productService.getProductChatInfo(productId);
		String genreName = productGenreFacade.getGenreName(product.getGenreId());
		boolean isMyProduct = product.getStoreId().equals(currentStoreId);

		// RequestParam 으로 roomId를 받지 않은 경우는 상품상세페이지에서 채팅뷰로 진입한 케이스로 채팅방 존재 여부 확인이 필요함
		if (roomId == null) {
			if (currentStoreId.equals(product.getStoreId())) {
				throw new NapzakException(ProductErrorCode.PRODUCT_CHAT_SELF_ROOM_ID_REQUIRED);
			}
			roomId = productChatFacade.findCommonRoomIdByStores(
				currentStoreId, product.getStoreId()).orElse(null);
		}
		// roomId가 null이면 채팅방 생성 전이고, 이 경우 api를 요청한 currentStore의 상대방은 무조건 product의 주인임
		// roomId(채팅방)가 존재한다면, roomId랑 currentStoreId로 채팅 상대방 chatParticipant 찾아서 상대방 storeId 가져옴
		Long chatOpponentStoreId = roomId == null ?
			product.getStoreId() : productChatFacade.findChatOpponentStoreId(roomId, currentStoreId);
		Store store = productStoreFacade.getStoreById(chatOpponentStoreId);

		BlockStatus blockStatus = productStoreFacade.getBlockStatus(currentStoreId, chatOpponentStoreId);
		
		ProductChatInfoResponse productChatInfoResponse = ProductChatInfoResponse
			.from(product, store, genreName, roomId, isMyProduct, blockStatus.isOpponentStoreBlocked(),
				blockStatus.isChatBlocked());

		return ResponseEntity.ok()
			.body(SuccessResponse.of(ProductSuccessCode.PRODUCT_CHAT_INFO_GET_SUCCESS, productChatInfoResponse));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/search/recommend")
	public ResponseEntity<SuccessResponse<RecommendResponse>> getRecommendSearchWordAndGenre(
		@CurrentMember Long currentStoreId
	) {
		List<RecommendSearchWordDto> recommendSearchWordDtoList = productService.getRecommendSearchWord();

		List<Genre> recommendGenreList = productGenreFacade.getRecommendGenre();
		List<RecommendGenreDto> recommendGenreDtoList = recommendGenreList.stream()
			.map(genre -> RecommendGenreDto.from(genre.getId(), genre.getName(), genre.getPhotoUrl()))
			.toList();

		RecommendResponse recommendResponse = new RecommendResponse(recommendSearchWordDtoList, recommendGenreDtoList);

		return ResponseEntity.ok()
			.body(
				SuccessResponse.of(ProductSuccessCode.RECOMMEND_SEARCH_WORD_AND_GENRE_GET_SUCCESS, recommendResponse));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PostMapping("/report/{productId}")
	public ResponseEntity<SuccessResponse<ProductReportResponse>> reportProduct(
		@PathVariable("productId") Long productId,
		@CurrentMember Long reporterId,
		@RequestBody @Valid ProductReportRequest request
	) {
		Product product = productService.getProduct(productId);
		List<ProductPhoto> photoList = productService.getProductPhotos(productId);

		productService.reportProduct(
			reporterId,
			product,
			photoList,
			request.reportTitle(),
			request.reportDescription(),
			request.reportContact()
		);

		ProductReportResponse response = ProductReportResponse.of(
			reporterId,
			product.getId(),
			request.reportTitle(),
			request.reportDescription(),
			request.reportContact()
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_REPORT_SUCCESS,
				response
			)
		);
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/interest/sell")
	public ResponseEntity<SuccessResponse<InterestedSellProductResponse>> getInterestedSellProducts(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long currentStoreId
	) {
		CursorValues cursorValues = parseCursorValues(cursor, ProductSortOption.INTEREST);

		// 1. 사용자의 좋아요 목록을 조회함 (최신순 정렬)
		List<Interest> interestList = productInterestFacade.findInterestsByStoreId(currentStoreId);

		// 2. 원하는 tradeType의 interest data를 추출해 <interestId, roomId> 로 구성
		Map<Long, Long> interestIdToProductIdMap = getInterestIdToproductIdMap(interestList, TradeType.SELL);

		// 3. pagination 생성
		ProductPagination pagination = productService.getInterestedProducts(
			interestIdToProductIdMap,
			cursorValues.getCursorId(),
			size
		);

		// 4. 다음 커서 생성을 위해 마지막 product와 해당 product의 interest id, interest createdAt을 구함
		Long lastProductId = pagination.getLastProductId();

		LocalDateTime lastInterestCreatedAt = getLastInterestLocalDateTime(lastProductId, currentStoreId);
		Long lastInterestId = getLastInterestId(lastProductId, currentStoreId);

		List<ProductWithFirstPhoto> productList = pagination.getProductList();

		Map<Long, String> genreMap = fetchGenreMap(pagination);
		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);

		// 5. 응답 생성
		InterestedSellProductResponse response = InterestedSellProductResponse.from(
			productList,
			interestMap,
			genreMap,
			currentStoreId,
			pagination,
			ProductSortOption.INTEREST,
			lastInterestId,
			lastInterestCreatedAt
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
			ProductSuccessCode.INTERESTED_PRODUCT_RETRIEVE_SUCCESS,
			response
		));

	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/interest/buy")
	public ResponseEntity<SuccessResponse<InterestedBuyProductResponse>> getInterestedBuyProducts(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long currentStoreId
	) {
		CursorValues cursorValues = parseCursorValues(cursor, ProductSortOption.INTEREST);

		// 1. 사용자의 좋아요 목록을 조회함 (최신순 정렬)
		List<Interest> interestList = productInterestFacade.findInterestsByStoreId(currentStoreId);

		// 2. 원하는 tradeType의 interest data를 추출해 <interestId, roomId> 로 구성
		Map<Long, Long> interestIdToProductIdMap = getInterestIdToproductIdMap(interestList, TradeType.BUY);

		// 3. pagination 생성
		ProductPagination pagination = productService.getInterestedProducts(
			interestIdToProductIdMap,
			cursorValues.getCursorId(),
			size
		);

		// 4. 다음 커서 생성을 위해 마지막 product와 해당 product의 interest id, interest createdAt을 구함
		Long lastProductId = pagination.getLastProductId();

		LocalDateTime lastInterestCreatedAt = getLastInterestLocalDateTime(lastProductId, currentStoreId);
		Long lastInterestId = getLastInterestId(lastProductId, currentStoreId);

		List<ProductWithFirstPhoto> productList = pagination.getProductList();

		Map<Long, String> genreMap = fetchGenreMap(pagination);
		Map<Long, Boolean> interestMap = fetchInterestMap(pagination, currentStoreId);

		// 5. 응답 생성
		InterestedBuyProductResponse response = InterestedBuyProductResponse.from(
			productList,
			interestMap,
			genreMap,
			currentStoreId,
			pagination,
			ProductSortOption.INTEREST,
			lastInterestId,
			lastInterestCreatedAt
		);

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.INTERESTED_PRODUCT_RETRIEVE_SUCCESS,
				response
			));
	}

	@AuthorizedRole({Role.ADMIN})
	@PostMapping("/clean")
	public ResponseEntity<SuccessResponse<Void>> productPhotoCleanUp(@CurrentMember Long currentStoreId) {
		Role currentStoreRole = productStoreFacade.getStoreRole(currentStoreId);
		if(currentStoreRole != Role.ADMIN) {
			throw new NapzakException(ProductErrorCode.ACCESS_DENIED);
		}

		productPhotoS3ImageCleaner.cleanUnusedProductImages();

		return ResponseEntity.ok(
			SuccessResponse.of(
				ProductSuccessCode.PRODUCT_PHOTO_DELETE_SUCCESS
			));
	}

	private void authChecker(Long currentStoreId, Product product) {

		if (!product.getStoreId().equals(currentStoreId)) {
			throw new NapzakException((ProductErrorCode.ACCESS_DENIED));
		}

	}

	private Map<Long, Boolean> fetchInterestMap(ProductPagination pagination, Long storeId) {
		List<Long> productIds = pagination.getProductList().stream()
			.map(ProductWithFirstPhoto::getId)
			.distinct()
			.toList();
		return productInterestFacade.getIsInterestedMap(productIds, storeId);
	}

	private Map<Long, String> fetchGenreMap(ProductPagination pagination) {
		List<Long> genreIds = pagination.getProductList().stream()
			.map(ProductWithFirstPhoto::getGenreId)
			.distinct()
			.toList();
		return productGenreFacade.getGenreNames(genreIds);
	}

	private ProductSortOption parseSortOption(String sortOption) {
		try {
			return ProductSortOption.valueOf(sortOption.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
		}
	}

	private CursorValues parseCursorValues(String cursor, ProductSortOption productSortOption) {
		if (cursor == null || cursor.isBlank()) {
			return new CursorValues(null, null);
		}

		try {
			switch (productSortOption) {
				case RECENT -> {
					Long id = RecentCursor.fromString(cursor).getid();
					return new CursorValues(id, null);
				}
				case POPULAR -> {
					PopularCursor popularCursor = PopularCursor.fromString(cursor);
					return new CursorValues(popularCursor.getId(), popularCursor.getInterestCount());
				}
				case LOW_PRICE -> {
					LowPriceCursor priceCursor = LowPriceCursor.fromString(cursor);
					return new CursorValues(priceCursor.getId(), priceCursor.getPrice());
				}
				case HIGH_PRICE -> {
					HighPriceCursor priceCursor = HighPriceCursor.fromString(cursor);
					return new CursorValues(priceCursor.getId(), priceCursor.getPrice());
				}
				case INTEREST -> {
					Long id = InterestCursor.fromString(cursor).getId();
					return new CursorValues(id, null);
				}
				default -> throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
			}
		} catch (IllegalArgumentException e) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
	}

	private static class CursorValues {
		private final Long cursorId;
		private final Integer cursorOptionalValue;

		public CursorValues(Long cursorId, Integer cursorOptionalValue) {
			this.cursorId = cursorId;
			this.cursorOptionalValue = cursorOptionalValue;
		}

		public Long getCursorId() {
			return cursorId;
		}

		public Integer getCursorOptionalValue() {
			return cursorOptionalValue;
		}
	}

	@Nullable
	private LocalDateTime getLastInterestLocalDateTime(Long lastProductId, Long storeId) {
		if (lastProductId == null) { return null; }
		return productInterestFacade.findInterestByProductIdAndStoreId(lastProductId, storeId).getCreatedAt();
	}

	@Nullable
	private Long getLastInterestId(Long lastProductId, Long storeId) {
		if (lastProductId == null) { return null; }
		return productInterestFacade.findInterestByProductIdAndStoreId(lastProductId, storeId).getId();
	}

	private Map<Long, Long> getInterestIdToproductIdMap(List<Interest> interestList, TradeType tradeType) {
		List<Long> productIds = interestList.stream().map(Interest::getProductId).distinct().toList();
		Set<Long> typeFilteredProductIds = productService.getTypeFilteredProductIdsByProductIds(productIds, tradeType);
		List<Interest> filteredInterestList = interestList.stream()
			.filter(interest -> typeFilteredProductIds.contains(interest.getProductId()))
			.toList();

		return filteredInterestList.stream()
			.collect(Collectors.toMap(Interest::getId, Interest::getProductId, (a, b) -> a, LinkedHashMap::new)
			);
	}
}
