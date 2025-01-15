package com.napzak.domain.genre.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = GenreTableConstants.TABLE_GENRE
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GenreTableConstants.COLUMN_ID)
    private Long id;

    @Column(name = GenreTableConstants.COLUMN_NAME, nullable = false)
    private String name;

    @Column(name = GenreTableConstants.COLUMN_PHOTO_URL, nullable = false)
    private String photoUrl;

    @Builder
    private GenreEntity(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public static GenreEntity create(
            final String name,
            final String photoUrl) {
        return GenreEntity.builder()
                .name(name)
                .photoUrl(photoUrl)
                .build();
    }
}