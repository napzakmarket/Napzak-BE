package com.napzak.api.domain.interest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.api.domain.interest.InterestProductFacade;
import com.napzak.api.domain.interest.code.InterestSuccessCode;
import com.napzak.api.domain.interest.service.InterestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/interest")
public class InterestController implements InterestApi {

	private final InterestService interestService;
	private final InterestProductFacade interestProductFacade;

	@Override
	@PostMapping("/{productId}")
	public ResponseEntity<SuccessResponse<Void>> postInterest(
		@PathVariable("productId") Long productId,
		@CurrentMember final Long storeId) {

		log.info("roomId: {}, currentUserId: {}", productId, storeId);

		interestService.postInterest(productId, storeId);
		interestProductFacade.incrementInterestCount(productId);

		return ResponseEntity.ok().body(SuccessResponse.of(InterestSuccessCode.POST_INTEREST_SUCCESS));
	}

	@Override
	@DeleteMapping("/{productId}")
	public ResponseEntity<SuccessResponse<Void>> deleteInterest(
		@PathVariable("productId") Long productId,
		@CurrentMember final Long storeId) {

		interestService.deleteInterest(productId, storeId);
		interestProductFacade.decrementInterestCount(productId);

		return ResponseEntity.ok().body(SuccessResponse.of(InterestSuccessCode.DELETE_INTEREST_SUCCESS));
	}

}
