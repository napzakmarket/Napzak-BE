package com.napzak.domain.review.core;

import com.napzak.domain.product.core.Product;
import com.napzak.domain.store.core.Store;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Review {
    private final Long id;
    private final int rate;
    private final String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Product product;
    private final Store reviewer;
    private final Store reviewee;

    public Review(Long id, int rate, String comment, LocalDateTime createdAt, LocalDateTime updatedAt, Product product, Store reviewer, Store reviewee) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.product = product;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
    }

    public static Review fromEntity(ReviewEntity reviewEntity){
        return new Review(
                reviewEntity.getId(),
                reviewEntity.getRate(),
                reviewEntity.getComment(),
                reviewEntity.getCreatedAt(),
                reviewEntity.getUpdatedAt(),
                Product.fromEntity(reviewEntity.getProduct()),
                Store.fromEntity(reviewEntity.getReviewer()),
                Store.fromEntity(reviewEntity.getReviewee())
        );
    }
}
