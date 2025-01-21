package com.napzak.domain.interest.api.service;

import org.springframework.stereotype.Service;

import com.napzak.domain.interest.api.exception.InterestErrorCode;
import com.napzak.domain.interest.core.InterestRemover;
import com.napzak.domain.interest.core.InterestRetriever;
import com.napzak.domain.interest.core.InterestSaver;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterestService {

	private final InterestSaver interestSaver;
	private final InterestRemover interestRemover;
	private final InterestRetriever interestRetriever;

	public void postInterest(Long productId, Long storeId) {

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
