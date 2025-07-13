package com.napzak.api.domain.genre.service;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.api.domain.genre.dto.request.cursor.OldestCursor;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;
import com.napzak.domain.genre.vo.Genre;
import com.napzak.domain.genre.vo.GenreList;
import com.napzak.domain.genre.entity.enums.GenreSortOption;

public class GenrePagination {
	private final int needSize;
	private final GenreList genreList;

	public GenrePagination(int needSize, GenreList genreList) {
		this.needSize = needSize;
		if (genreList == null) {
			this.genreList = new GenreList(List.of());
		} else {
			this.genreList = genreList;
		}
	}

	private boolean hasMoreData() {
		return genreList.size() > needSize;
	}

	@Nullable
	public String generateNextCursor(GenreSortOption genreSortOption) {
		if (!hasMoreData()) {
			return null;
		}
		Genre lastGenre = genreList.getGenreList().get(needSize);
		switch (genreSortOption) {
			case OLDEST:
				return new OldestCursor(lastGenre.getId()).toString();
			default:
				throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
		}
	}

	public List<Genre> getGenreList() {
		return genreList.getGenreList().subList(0, Math.min(needSize, genreList.size()));
	}
}
