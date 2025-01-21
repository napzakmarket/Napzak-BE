package com.napzak.domain.product.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.napzak.domain.review.core.ReviewRetriever;
import com.napzak.domain.review.core.vo.Review;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductReviewFacade {

	private final ReviewRetriever reviewRetriever;

	public final List<Review> findAllByStoreId(final Long storeId) {
		return reviewRetriever.retrieveAllByStoreId(storeId);
	}

	public final Map<Long, String> findReviewerNamesByReviewId(final Long storeId, final List<Long> reviewIds) {
		List<Object[]> results = reviewRetriever.findReviewerNicknamesByStoreIdAndReviewIds(storeId, reviewIds);
		return results.stream()
			.collect(Collectors.toMap(
				result -> (Long)result[0],
				result -> (String)result[1]
			));
	}
}
