package com.napzak.domain.product.api;

import com.napzak.domain.store.core.GenrePreferenceRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductStoreFacade {

    private final GenrePreferenceRetriever genrePreferenceRetriever;

    public Boolean findGenrePreference(Long storeId){
        return genrePreferenceRetriever.findGenrePreference(storeId);
    }

}
