package com.napzak.domain.product.api;

import com.napzak.domain.store.core.GenrePreferenceRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductStoreFacade {

    private final GenrePreferenceRetriever genrePreferenceRetriever;

    public List<Long> getGenrePreferenceIds(Long storeId) { return genrePreferenceRetriever.getGenrePreferenceIds(storeId);}
}