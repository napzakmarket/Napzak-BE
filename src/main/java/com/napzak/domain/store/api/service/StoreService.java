package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {
    private final StoreRetriever storeRetriever;

    @Transactional(readOnly = true)
    public Store findStoreByStoreId(Long StoreId) {
        return storeRetriever.findStoreByStoreId(StoreId);

    }

    @Transactional(readOnly = true)
    public boolean checkStoreExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return storeRetriever.checkStoreExistsBySocialIdAndSocialType(socialId, socialType);
    }

    @Transactional(readOnly = true)
    public Store findStoreBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return storeRetriever.findBySocialTypeAndSocialId(socialId, socialType);
    }

}