package com.napzak.domain.product.api.exception;

import org.springframework.http.HttpStatus;

import com.napzak.global.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	PRODUCT_RETRIEVE_SUCCESS(HttpStatus.OK, "상품이 조회되었습니다."),
	PRODUCT_LIST_RETRIEVE_SUCCESS(HttpStatus.OK, "상품 목록이 조회되었습니다."),
	PRODUCT_LIST_SEARCH_SUCCESS(HttpStatus.OK, "검색된 상품 목록이 조회되었습니다."),
	RECOMMEND_PRODUCT_GET_SUCCESS(HttpStatus.OK, "개인화 상품 목록이 조회되었습니다."),
	TOP_SELL_PRODUCT_GET_SUCCESS(HttpStatus.OK, "팔아요 인기 상품 목록이 조회되었습니다."),
	TOP_BUY_PRODUCT_GET_SUCCESS(HttpStatus.OK, "구해요 인기 상품 목록이 조회되었습니다."),
	PRODUCT_DETAIL_SUCCESS(HttpStatus.OK, "상품 상세 정보가 조회되었습니다."),
	PRODUCT_UPDATE_SUCCESS(HttpStatus.OK, "상품 상태 변경이 성공하였습니다."),
	RECOMMEND_SEARCH_WORD_AND_GENRE_GET_SUCCESS(HttpStatus.OK, "추천 검색어 및 추천 장르가 조회되었습니다."),

	/*
	201 Created
	 */
	PRODUCT_CREATE_SUCCESS(HttpStatus.CREATED, "상품이 등록되었습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
