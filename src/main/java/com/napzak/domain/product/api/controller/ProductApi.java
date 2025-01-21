package com.napzak.domain.product.api.controller;

import com.napzak.domain.product.api.dto.request.ProductBuyCreateRequest;
import com.napzak.domain.product.api.dto.request.ProductSellCreateRequest;
import com.napzak.domain.product.api.dto.response.*;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Product", description = "상품 관련 API")
@RequestMapping("api/v1/products")
public interface ProductApi {

	@Operation(summary = "판매 상품 목록 조회", description = "판매 상품 목록을 필터링 및 페이징하여 조회")
	@GetMapping("/sell")
	ResponseEntity<SuccessResponse<ProductSellListResponse>> getSellProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(defaultValue = "false") Boolean isUnopened,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	);

	@Operation(summary = "구매 상품 목록 조회", description = "구매 상품 목록을 필터링 및 페이징하여 조회")
	@GetMapping("/buy")
	ResponseEntity<SuccessResponse<ProductBuyListResponse>> getBuyProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	);

	@Operation(summary = "판매 상품 검색", description = "검색어를 기반으로 판매 상품 조회")
	@GetMapping("/sell/search")
	ResponseEntity<SuccessResponse<ProductSellListResponse>> searchSellProducts(
		@RequestParam String searchWord,
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(defaultValue = "false") Boolean isUnopened,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	);

	@Operation(summary = "구매 상품 검색", description = "검색어를 기반으로 구매 상품 조회")
	@GetMapping("/buy/search")
	ResponseEntity<SuccessResponse<ProductBuyListResponse>> searchBuyProducts(
		@RequestParam String searchWord,
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	);

	@Operation(summary = "상점의 판매 상품 목록 조회", description = "특정 상점의 판매 상품을 조회")
	@GetMapping("/sell/stores/{storeOwnerId}")
	ResponseEntity<SuccessResponse<ProductSellListResponse>> getStoreSellProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(defaultValue = "false") Boolean isUnopened,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@PathVariable Long storeOwnerId,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "상점의 구매 상품 목록 조회", description = "특정 상점의 구매 상품을 조회")
	@GetMapping("/buy/stores/{storeOwnerId}")
	ResponseEntity<SuccessResponse<ProductBuyListResponse>> getStoreBuyProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@PathVariable Long storeOwnerId,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "판매 상품 등록", description = "판매 상품 등록 API")
	@PostMapping("/sell")
	ResponseEntity<SuccessResponse<ProductSellResponse>> createSellProduct(
		@Valid @RequestBody ProductSellCreateRequest productSellCreateRequest,
		@CurrentMember Long storeId
	);

	@Operation(summary = "구매 상품 등록", description = "구매 상품 등록 API")
	@PostMapping("/buy")
	ResponseEntity<SuccessResponse<ProductBuyResponse>> createBuyProduct(
		@Valid @RequestBody ProductBuyCreateRequest productBuyCreateRequest,
		@CurrentMember Long storeId
	);

	@Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회")
	@GetMapping("/{productId}")
	ResponseEntity<SuccessResponse<ProductDetailResponse>> getDetailProductInfo(
		@PathVariable("productId") Long productId,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "추천 상품 조회", description = "사용자의 관심 기반 추천 상품 조회")
	@GetMapping("/home/recommend")
	ResponseEntity<SuccessResponse<ProductRecommendListResponse>> getRecommendProducts(
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "홈 화면 인기 판매 상품 조회", description = "홈 화면에 인기 판매 상품을 조회")
	@GetMapping("/home/sell")
	ResponseEntity<SuccessResponse<ProductSellListResponse>> getTopSellProducts(
		@RequestParam(defaultValue = "4") int size,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "홈 화면 인기 구매 상품 조회", description = "홈 화면에 인기 구매 상품을 조회")
	@GetMapping("/home/buy")
	ResponseEntity<SuccessResponse<ProductBuyListResponse>> getTopBuyProducts(
		@RequestParam(defaultValue = "4") int size,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "상품 채팅 정보 조회", description = "채팅방 생성 시 필요한 상품 정보 조회")
	@GetMapping("/chat/{productId}")
	ResponseEntity<SuccessResponse<ProductChatResponse>> getProductChatInfo(
		@PathVariable Long productId,
		@CurrentMember Long storeId
	);
}
