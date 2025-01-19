package com.napzak.domain.store.api;

import com.napzak.domain.genre.core.GenreRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreGenreFacade {

    private final GenreRetriever genreRetriever;

    public String getGenreName(Long genreId) {
        return genreRetriever.retrieveGenreNameById(genreId);
    }
}
