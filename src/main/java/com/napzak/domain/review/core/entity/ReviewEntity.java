package com.napzak.domain.review.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.review.core.entity.ReviewTableConstants.*;

@Table(name = TABLE_REVIEW)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_RATE, nullable = false)
	private int rate;

	@Column(name = COLUMN_COMMENT, nullable = false)
	private String comment;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = COLUMN_UPDATED_AT, nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now();

	@Column(name = COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = COLUMN_REVIEWER_ID, nullable = false)
	private Long reviewerId;

	@Column(name = COLUMN_REVIEWEE_ID, nullable = false)
	private Long revieweeId;

	@Builder
	private ReviewEntity(int rate, String comment, Long productId, Long reviewerId, Long revieweeId) {
		this.rate = rate;
		this.comment = comment;
		this.productId = productId;
		this.reviewerId = reviewerId;
		this.revieweeId = revieweeId;
	}
}