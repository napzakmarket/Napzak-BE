package com.napzak.domain.product.crud.searchword;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.repository.SearchWordRepository;
import com.napzak.domain.product.vo.SearchWord;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchWordRetriever {
	private final SearchWordRepository searchWordRepository;

	public List<SearchWord> findAll() {
		return searchWordRepository.findAll()
			.stream()
			.map(SearchWord::fromEntity)
			.toList();
	}
}
