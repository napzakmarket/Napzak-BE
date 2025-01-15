package com.napzak.domain.store.core.vo;

import com.napzak.domain.genre.core.vo.Genre;
import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import lombok.Getter;

@Getter
public class GenrePreference {
    private final Long id;
    private final Store store;
    private final Genre genre;

    public GenrePreference(Long id, Store store, Genre genre) {
        this.id = id;
        this.store = store;
        this.genre = genre;
    }

    public static GenrePreference fromEntity(GenrePreferenceEntity genrePreferenceEntity) {
        return new GenrePreference(
                genrePreferenceEntity.getId(),
                Store.fromEntity(genrePreferenceEntity.getStoreEntity()),
                Genre.fromEntity(genrePreferenceEntity.getGenreEntity())
        );
    }
}