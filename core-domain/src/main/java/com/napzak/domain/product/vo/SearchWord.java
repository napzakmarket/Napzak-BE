package com.napzak.domain.product.vo;

import com.napzak.domain.product.entity.SearchWordEntity;

import lombok.Getter;

@Getter
public class SearchWord {
	private final Long id;
	private final String searchWord;

	public SearchWord(Long id, String searchWord) {
		this.id = id;
		this.searchWord = searchWord;
	}

	public static SearchWord fromEntity(SearchWordEntity searchWordEntity) {
		return new SearchWord(
			searchWordEntity.getId(),
			searchWordEntity.getSearchWord()
		);
	}
}
