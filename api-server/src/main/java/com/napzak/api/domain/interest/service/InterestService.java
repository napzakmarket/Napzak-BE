package com.napzak.api.domain.interest.service;

import org.springframework.stereotype.Service;

import com.napzak.domain.interest.code.InterestErrorCode;
import com.napzak.common.exception.NapzakException;
import com.napzak.domain.interest.crud.InterestRemover;
import com.napzak.domain.interest.crud.InterestRetriever;
import com.napzak.domain.interest.crud.InterestSaver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterestService {

	private final InterestSaver interestSaver;
	private final InterestRemover interestRemover;
	private final InterestRetriever interestRetriever;

	public void postInterest(Long productId, Long storeId) {

		log.info("now in interestServce. roomId : {}, storeId : {}");

		if (interestRetriever.getIsInterested(productId, storeId)) {//이미 좋아요가 눌러져 있음
			throw new NapzakException(InterestErrorCode.INTEREST_ALREADY_POSTED);
		}

		interestSaver.saveInterest(productId, storeId);
	}

	public void deleteInterest(Long productId, Long storeId) {

		if (!interestRetriever.getIsInterested(productId, storeId)) { //좋아요가 눌려져 있지 않은 상태임
			throw new NapzakException(InterestErrorCode.INTEREST_NOT_FOUND);
		}

		interestRemover.deleteInterest(productId, storeId);
	}

}
