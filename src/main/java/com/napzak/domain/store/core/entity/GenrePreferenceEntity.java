package com.napzak.domain.store.core.entity;

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

	@Column(name = COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Column(name = COLUMN_GENRE_ID, nullable = false)
	private Long genreId;

	@Builder
	private GenrePreferenceEntity(Long storeId, Long genreId) {
		this.storeId = storeId;
		this.genreId = genreId;
	}
}