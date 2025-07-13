package com.napzak.chat.domain.chat.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.genre.crud.GenreRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatGenreFacade {

	private final GenreRetriever genreRetriever;
}
