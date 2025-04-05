package com.napzak.domain.product.core.entity;

import static com.napzak.domain.product.core.entity.SearchWordTableConstants.*;

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

@Table(name = TABLE_SEARCH_WORD)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchWordEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_SEARCH_WORD, nullable = false)
	private String searchWord;

	@Builder
	public SearchWordEntity(String searchWord) {
		this.searchWord = searchWord;
	}

	public static SearchWordEntity create(String searchWord) {
		return SearchWordEntity.builder()
			.searchWord(searchWord)
			.build();
	}
}
