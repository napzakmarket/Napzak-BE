package com.napzak.domain.store.core;

import com.napzak.domain.store.core.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenrePreferenceRetriever {

    private final GenrePreferenceRepository genrePreferenceRepository;

    public Boolean findGenrePreference(Long storeId) {
        return genrePreferenceRepository.existsByStoreEntityId(storeId);

    }
}
