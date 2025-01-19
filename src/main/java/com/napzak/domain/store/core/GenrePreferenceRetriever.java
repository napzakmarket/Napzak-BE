package com.napzak.domain.store.core;

import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import com.napzak.domain.store.core.vo.GenrePreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenrePreferenceRetriever {

    private final GenrePreferenceRepository genrePreferenceRepository;

    public Boolean existGenrePreference(Long storeId) {
        return genrePreferenceRepository.existsByStoreId(storeId);
    }

    public List<GenrePreference> getGenrePreferences(Long storeId) {
        List<GenrePreferenceEntity> genrePreferenceEntityList = genrePreferenceRepository.findByStoreId(storeId);
        return genrePreferenceEntityList.stream()
                .map(GenrePreference::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Long> getGenrePreferenceIds(Long storeId) {
        return genrePreferenceRepository.findGenreIdsByStoreId(storeId);
    }
}
