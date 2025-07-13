package com.napzak.domain.genre.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.annotation.Nullable;

public class GenreList {
	private final List<Genre> genreList;

	public GenreList(List<Genre> genreList) {
		this.genreList = Objects.requireNonNullElseGet(genreList, ArrayList::new);
	}

	@Nullable
	public Genre first() {
		return genreList.stream()
			.findFirst()
			.orElseGet(() -> null);
	}

	@Nullable
	public Genre last() {
		if (genreList.isEmpty()) {
			return null;
		}

		final int lastIndex = genreList.size() - 1;
		return genreList.get(lastIndex);
	}

	public int size() {
		return genreList.size();
	}

	public boolean isEmpty() {
		return genreList.isEmpty();
	}

	public List<Genre> getGenreList() {
		return genreList;
	}
}
