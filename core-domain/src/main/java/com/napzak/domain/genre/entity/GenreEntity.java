package com.napzak.domain.genre.entity;

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

	@Column(name = GenreTableConstants.COLUMN_COVER, nullable = false)
	private String cover;

	@Column(name = GenreTableConstants.COLUMN_TAG, nullable = true)
	private String tag;

	@Column(name = GenreTableConstants.COLUMN_RECOMMEND_ORDER, nullable = false)
	private int recommendOrder = 0;

	@Builder
	private GenreEntity(String name, String photoUrl, String cover, String tag, int recommendOrder) {
		this.name = name;
		this.photoUrl = photoUrl;
		this.cover = cover;
		this.tag = tag;
		this.recommendOrder = recommendOrder;
	}

	public static GenreEntity create(
		final String name,
		final String photoUrl,
		final String cover,
		final String tag,
		final int recommendOrder) {
		return GenreEntity.builder()
			.name(name)
			.photoUrl(photoUrl)
			.cover(cover)
			.tag(tag)
			.recommendOrder(recommendOrder)
			.build();
	}
}