package com.napzak.domain.review.vo;

import com.napzak.domain.review.entity.ReviewEntity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Review {
	private final Long id;
	private final int rate;
	private final String comment;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final Long productId;
	private final Long reviewerId;
	private final Long revieweeId;

	public Review(Long id, int rate, String comment, LocalDateTime createdAt, LocalDateTime updatedAt,
		Long productId, Long reviewerId, Long revieweeId) {
		this.id = id;
		this.rate = rate;
		this.comment = comment;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.productId = productId;
		this.reviewerId = reviewerId;
		this.revieweeId = revieweeId;
	}

	public static Review fromEntity(ReviewEntity reviewEntity) {
		return new Review(
			reviewEntity.getId(),
			reviewEntity.getRate(),
			reviewEntity.getComment(),
			reviewEntity.getCreatedAt(),
			reviewEntity.getUpdatedAt(),
			reviewEntity.getProductId(),
			reviewEntity.getReviewerId(),
			reviewEntity.getRevieweeId()
		);
	}
}
