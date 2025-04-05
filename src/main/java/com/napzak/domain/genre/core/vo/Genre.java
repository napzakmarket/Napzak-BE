package com.napzak.domain.genre.core.vo;

import com.napzak.domain.genre.core.entity.GenreEntity;

import lombok.Getter;

@Getter
public class Genre {
	private final Long id;
	private final String name;
	private final String photoUrl;
	private final String cover;
	private final String tag;
	private final int recommendOrder;

	public Genre(Long id, String name, String photoUrl, String cover, String tag, int recommendOrder) {
		this.id = id;
		this.name = name;
		this.photoUrl = photoUrl;
		this.cover = cover;
		this.tag = tag;
		this.recommendOrder = recommendOrder;
	}

	public static Genre fromEntity(GenreEntity genreEntity) {
		return new Genre(
			genreEntity.getId(),
			genreEntity.getName(),
			genreEntity.getPhotoUrl(),
			genreEntity.getCover(),
			genreEntity.getTag(),
			genreEntity.getRecommendOrder()
		);
	}
}