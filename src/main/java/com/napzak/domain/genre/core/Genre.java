package com.napzak.domain.genre.core;
import lombok.Getter;

@Getter
public class Genre {
    private final Long id;
    private final String name;
    private final String photoUrl;

    public Genre(Long id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public static Genre fromEntity(GenreEntity genreEntity) {
        return new Genre(
                genreEntity.getId(),
                genreEntity.getName(),
                genreEntity.getPhotoUrl()
        );
    }
}