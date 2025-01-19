package com.napzak.domain.store.api.service;

import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.api.dto.GenrePreferenceResponse;
import com.napzak.domain.store.api.dto.MyPageResponse;
import com.napzak.domain.store.api.dto.StoreInfoResponse;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.GenrePreference;
import com.napzak.domain.store.core.vo.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {
    private final StoreRetriever storeRetriever;
    private final GenrePreferenceRetriever genrePreferenceRetriever;
    private final StoreGenreFacade storeGenreFacade;

    @Transactional(readOnly = true)
    public boolean checkStoreExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return storeRetriever.checkStoreExistsBySocialIdAndSocialType(socialId, socialType);
    }

    @Transactional(readOnly = true)
    public Store findStoreBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return storeRetriever.findBySocialTypeAndSocialId(socialId, socialType);
    }

    @Transactional(readOnly = true)
    public MyPageResponse getMyPageInfo(final Long storeId) {
        Store store = storeRetriever.findStoreByStoreId(storeId);
        return new MyPageResponse(store.getId(), store.getNickname(), store.getPhoto());
    }

    @Transactional(readOnly = true)
    public StoreInfoResponse getStoreInfo(final Long storeId) {
        Store store = storeRetriever.findStoreByStoreId(storeId);
        List<GenrePreference> genrePreferenceList = genrePreferenceRetriever.getGenrePreferences(storeId);

        List<GenrePreferenceResponse> genrePreferenceResponses = genrePreferenceList.stream()
                .map(genrePreference -> GenrePreferenceResponse.of(
                        genrePreference.getGenreId(),
                        storeGenreFacade.getGenreName(genrePreference.getGenreId())
                ))
                .toList();

        return StoreInfoResponse.of(
                store.getId(),
                store.getNickname(),
                store.getDescription(),
                store.getPhoto(),
                genrePreferenceResponses
        );
    }

}