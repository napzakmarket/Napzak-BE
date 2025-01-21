package com.napzak.domain.store.core.entity;

import static com.napzak.domain.store.core.entity.GenrePreferenceTableConstants.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public static GenrePreferenceEntity create(Long storeId, Long genreId) {
		return GenrePreferenceEntity.builder()
			.storeId(storeId)
			.genreId(genreId)
			.build();
	}

}