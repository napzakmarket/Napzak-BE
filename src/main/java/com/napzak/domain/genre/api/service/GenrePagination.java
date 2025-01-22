package com.napzak.domain.genre.api.service;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.domain.genre.api.dto.request.cursor.OldestCursor;
import com.napzak.domain.genre.api.exception.GenreErrorCode;
import com.napzak.domain.genre.core.vo.Genre;
import com.napzak.domain.genre.core.vo.GenreList;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.code.ErrorCode;
import com.napzak.domain.genre.api.service.enums.SortOption;

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
	public String generateNextCursor(SortOption sortOption) {
		if (!hasMoreData()) {
			return null;
		}
		Genre lastGenre = genreList.getGenreList().get(needSize);
		switch (sortOption) {
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
