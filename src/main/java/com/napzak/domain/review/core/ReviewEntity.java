package com.napzak.domain.review.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.review.core.ReviewTableConstants.*;

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

    @ManyToOne
    @JoinColumn(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = COLUMN_REVIEWER_ID, nullable = false)
    private StoreEntity reviewer;

    @ManyToOne
    @JoinColumn(name = COLUMN_REVIEWEE_ID, nullable = false)
    private StoreEntity reviewee;

    @Builder
    private ReviewEntity(
            int rate,
            String comment,
            ProductEntity product,
            StoreEntity reviewer,
            StoreEntity reviewee
    ) {
        this.rate = rate;
        this.comment = comment;
        this.product = product;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
    }

    public static ReviewEntity create(
            final int rate,
            final String comment,
            final ProductEntity product,
            final StoreEntity reviewer,
            final StoreEntity reviewee
    ) {
        return ReviewEntity.builder()
                .rate(rate)
                .comment(comment)
                .product(product)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .build();
    }
}