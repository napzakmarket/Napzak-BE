package com.napzak.domain.banner.core;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.banner.core.BannerTableConstants.*;

@Table(name = TABLE_BANNER)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_PHOTO_URL, nullable = false)
    private String photoUrl;

    @Column(name = COLUMN_SEQUENCE, nullable = false)
    private int sequence;

    @Column(name = COLUMN_REDIRECT_URL, nullable = true)
    private String redirectUrl;

    @Column(name = COLUMN_CREATED_AT, nullable = true)
    private LocalDateTime createdAt;

    @Column(name = COLUMN_UPDATED_AT, nullable = true)
    private LocalDateTime updatedAt;

    @Builder
    private BannerEntity(String photoUrl, int sequence, String redirectUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.photoUrl = photoUrl;
        this.sequence = sequence;
        this.redirectUrl = redirectUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BannerEntity create(
            final String photoUrl,
            final int sequence,
            final String redirectUrl
    ) {
        return BannerEntity.builder()
                .photoUrl(photoUrl)
                .sequence(sequence)
                .redirectUrl(redirectUrl)
                .build();
    }

}