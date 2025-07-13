package com.napzak.domain.review.crud;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.review.entity.ReviewEntity;
import com.napzak.domain.review.repository.ReviewRepository;
import com.napzak.domain.review.vo.Review;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewRetriever {

	private final ReviewRepository reviewRepository;

	public List<Review> retrieveAllByStoreId(Long storeId) {
		List<ReviewEntity> reviewEntityList = reviewRepository.findAllByStoreId(storeId);
		return reviewEntityList.stream()
			.map(Review::fromEntity)
			.toList();
	}

	public List<Object[]> findReviewerNicknamesByStoreIdAndReviewIds(Long storeId, List<Long> reviewIds) {
		return reviewRepository.findReviewerNicknamesByStoreIdAndReviewIds(storeId, reviewIds);
	}

}
