package com.napzak.api.domain.product.controller;

import com.napzak.api.domain.product.dto.request.ProductBuyCreateRequest;
import com.napzak.api.domain.product.dto.request.ProductReportRequest;
import com.napzak.api.domain.product.dto.request.ProductSellCreateRequest;
import com.napzak.api.domain.product.dto.request.TradeStatusRequest;
import com.napzak.api.domain.product.dto.response.ProductBuyListResponse;
import com.napzak.api.domain.product.dto.response.ProductBuyModifyResponse;
import com.napzak.api.domain.product.dto.response.ProductBuyResponse;
import com.napzak.api.domain.product.dto.response.ProductChatInfoResponse;
import com.napzak.api.domain.product.dto.response.ProductDetailResponse;
import com.napzak.api.domain.product.dto.response.ProductRecommendListResponse;
import com.napzak.api.domain.product.dto.response.ProductReportResponse;
import com.napzak.api.domain.product.dto.response.ProductSellListResponse;
import com.napzak.api.domain.product.dto.response.ProductSellModifyResponse;
import com.napzak.api.domain.product.dto.response.ProductSellResponse;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.exception.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Product", description = "상품 관련 API")
@RequestMapping("api/v1/products")
public interface ProductApi {

	@Operation(summary = "팔아요 상품 목록 조회", description = "팔아요 상품 목록을 필터링 및 페이징하여 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "팔아요 상품 목록 조회 성공"),
		@ApiResponse(responseCode = "404", description = "조회된 상품이 없습니다.")
	})
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

	@Operation(summary = "구해요 상품 목록 조회", description = "구해요 상품 목록을 필터링 및 페이징하여 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "구해요 상품 목록 조회 성공"),
		@ApiResponse(responseCode = "404", description = "조회된 상품이 없습니다.")
	})
	@GetMapping("/buy")
	ResponseEntity<SuccessResponse<ProductBuyListResponse>> getBuyProducts(
		@RequestParam(defaultValue = "RECENT") String sortOption,
		@RequestParam(defaultValue = "false") Boolean isOnSale,
		@RequestParam(required = false) List<Long> genreId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	);

	@Operation(summary = "팔아요 상품 검색", description = "검색어를 기반으로 팔아요 상품 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "검색된 팔아요 상품 조회 성공"),
		@ApiResponse(responseCode = "400", description = "검색어가 누락되었습니다."),
		@ApiResponse(responseCode = "404", description = "검색 결과가 없습니다.")
	})
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

	@Operation(summary = "구해요 상품 검색", description = "검색어를 기반으로 구해요 상품 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "검색된 구해요 상품 조회 성공"),
		@ApiResponse(responseCode = "400", description = "검색어가 누락되었습니다."),
		@ApiResponse(responseCode = "404", description = "검색 결과가 없습니다.")
	})
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

	@Operation(summary = "상점의 팔아요 상품 목록 조회", description = "특정 상점의 팔아요 상품을 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상점 팔아요 상품 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
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

	@Operation(summary = "상점의 구해요 상품 목록 조회", description = "특정 상점의 구해요 상품을 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상점 구해요 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
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

	@Operation(summary = "팔아요 상품 등록", description = "팔아요 상품 등록 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "팔아요 상품 등록 성공"),
		@ApiResponse(responseCode = "400", description = "필수 필드가 누락되었습니다.")
	})
	@PostMapping("/sell")
	ResponseEntity<SuccessResponse<ProductSellResponse>> createSellProduct(
		@Valid @RequestBody ProductSellCreateRequest productSellCreateRequest,
		@CurrentMember Long storeId
	);

	@Operation(summary = "구해요 상품 등록", description = "구해요 상품 등록 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "구해요 상품 등록 성공"),
		@ApiResponse(responseCode = "400", description = "필수 필드가 누락되었습니다.")
	})
	@PostMapping("/buy")
	ResponseEntity<SuccessResponse<ProductBuyResponse>> createBuyProduct(
		@Valid @RequestBody ProductBuyCreateRequest productBuyCreateRequest,
		@CurrentMember Long storeId
	);

	@Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 상세 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@GetMapping("/{productId}")
	ResponseEntity<SuccessResponse<ProductDetailResponse>> getDetailProductInfo(
		@PathVariable("productId") Long productId,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "상품 거래 상태 수정", description = "상품의 거래 상태(tradeStatus)를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 거래 상태 수정 성공"),
		@ApiResponse(responseCode = "403", description = "상품 소유자가 아닙니다."),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@PatchMapping("/{productId}")
	ResponseEntity<SuccessResponse<Void>> updateTradeStatus(
		@CurrentMember Long currentStoreId,
		@PathVariable("productId") Long productId,
		@Valid @RequestBody TradeStatusRequest tradeStatusRequest
	);

	@Operation(summary = "상품 삭제", description = "상품 데이터를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 거래 상태 수정 성공"),
		@ApiResponse(responseCode = "403", description = "상품 소유자가 아닙니다."),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@DeleteMapping("/{productId}")
	ResponseEntity<SuccessResponse<Void>> deleteProduct(
		@CurrentMember Long currentStoreId,
		@PathVariable("productId") Long productId
	);

	@Operation(summary = "수정을 위한 팔아요 상품 정보 조회", description = "상품 수정을 위한 팔아요 상품 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 거래 상태 수정 성공"),
		@ApiResponse(responseCode = "403", description = "상품 소유자가 아닙니다."),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@DeleteMapping("/sell/modify/{productId}")
	ResponseEntity<SuccessResponse<ProductSellModifyResponse>> getSellProductForModify(
		@CurrentMember Long currentStoreId,
		@PathVariable("productId") Long productId
	);

	@Operation(summary = "수정을 위한 구해요 상품 정보 조회", description = "상품 수정을 위한 구해요 상품 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 거래 상태 수정 성공"),
		@ApiResponse(responseCode = "403", description = "상품 소유자가 아닙니다."),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@DeleteMapping("/buy/modify/{productId}")
	ResponseEntity<SuccessResponse<ProductBuyModifyResponse>> getBuyProductForModify(
		@CurrentMember Long currentStoreId,
		@PathVariable("productId") Long productId
	);


	@Operation(summary = "추천 상품 조회", description = "사용자의 관심 기반 추천 상품 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자의 관심 기반 추천 상품 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@GetMapping("/home/recommend")
	ResponseEntity<SuccessResponse<ProductRecommendListResponse>> getRecommendProducts(
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "홈 화면 인기 팔아요 상품 조회", description = "홈 화면에 인기 팔아요 상품을 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "홈화면 인기 팔아요 상품 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@GetMapping("/home/sell")
	ResponseEntity<SuccessResponse<ProductSellListResponse>> getTopSellProducts(
		@RequestParam(defaultValue = "4") int size,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "홈 화면 인기 구해요 상품 조회", description = "홈 화면에 인기 구해요 상품을 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "홈화면 인기 구해요 상품 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@GetMapping("/home/buy")
	ResponseEntity<SuccessResponse<ProductBuyListResponse>> getTopBuyProducts(
		@RequestParam(defaultValue = "4") int size,
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "상품 신고", description = "특정 상품을 신고하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 신고 성공",
			content = @Content(schema = @Schema(implementation = ProductReportResponse.class))),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@PostMapping("/report/{productId}")
	ResponseEntity<SuccessResponse<ProductReportResponse>> reportProduct(
		@Parameter(description = "신고 대상 상품 ID")
		@PathVariable("productId") Long productId,
		@CurrentMember Long reporterId,
		@RequestBody @Valid ProductReportRequest request
	);

	@Operation(summary = "채팅 대상 상품 정보 조회", description = "채팅방 생성 시 필요한 상품 정보 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "채팅방 생성 시 필요한 상품 정보 조회 성공"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.")
	})
	@GetMapping("/chat/{productId}")
	ResponseEntity<SuccessResponse<ProductChatInfoResponse>> getProductChatInfo(
		@PathVariable Long productId,
		@CurrentMember Long storeId
	);

	@Operation(summary = "사용하지 않는 S3 product 이미지 삭제", description = "사용하지 않는 s3 이미지를 일괄 삭제")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "s3 이미지 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.")
	})
	@PostMapping("/clean")
	ResponseEntity<SuccessResponse<Void>> productPhotoCleanUp(
		@CurrentMember Long currentStoreId
	);
}
