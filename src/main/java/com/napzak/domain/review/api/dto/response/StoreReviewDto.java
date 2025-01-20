package com.napzak.domain.review.api.dto.response;

import com.napzak.domain.product.core.vo.Product;
import com.napzak.domain.review.core.vo.Review;

public record StoreReviewDto(
	Long reviewId,
	String reviewerNickname,
	float rating,
	String comment,
	Long relatedProductId,
	String relatedProductName
) {
	public static StoreReviewDto from(
		Review review,
		String reviewerNickname,
		Product product
	) {
		return new StoreReviewDto(
			review.getId(),
			reviewerNickname,
			review.getRate(),
			review.getComment(),
			product.getId(),
			product.getTitle()
		);
	}
}
