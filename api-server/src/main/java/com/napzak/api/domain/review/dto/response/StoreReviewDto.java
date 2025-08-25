package com.napzak.api.domain.review.dto.response;

import com.napzak.domain.product.vo.Product;
import com.napzak.domain.review.vo.Review;

public record StoreReviewDto(
	Long reviewId,
	String reviewerNickname,
	int rating,
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
