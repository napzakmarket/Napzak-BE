package com.napzak.domain.store.core.entity;

import com.napzak.domain.genre.core.entity.GenreEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.store.core.entity.GenrePreferenceTableConstants.*;


@Entity
@Table(name = TABLE_GENRE_PREFERENCE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenrePreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_GENRE_PREFERENCE_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_STORE_ID, nullable = false)
    private StoreEntity storeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_GENRE_ID, nullable = false)
    private GenreEntity genreEntity;

    @Builder
    private GenrePreferenceEntity(StoreEntity storeEntity, GenreEntity genreEntity) {
        this.storeEntity = storeEntity;
        this.genreEntity = genreEntity;
    }

    public static GenrePreferenceEntity create(
            final StoreEntity storeEntity,
            final GenreEntity genreEntity
    ) {
        return GenrePreferenceEntity.builder()
                .storeEntity(storeEntity)
                .genreEntity(genreEntity)
                .build();
    }
}