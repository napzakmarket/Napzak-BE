package com.napzak.domain.banner.core;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Banner {
    private final Long id;
    private final String photoUrl;
    private final int sequence;
    private final String redirectUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Banner(Long id, String photoUrl, int sequence, String redirectUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.sequence = sequence;
        this.redirectUrl = redirectUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Banner fromEntity(BannerEntity bannerEntity) {
        return new Banner(
                bannerEntity.getId(),
                bannerEntity.getPhotoUrl(),
                bannerEntity.getSequence(),
                bannerEntity.getRedirectUrl(),
                bannerEntity.getCreatedAt(),
                bannerEntity.getUpdatedAt()
        );
    }
}