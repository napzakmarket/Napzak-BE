package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.StoreSaver;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.StoreRepository;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRegistrationService {

    private final StoreSaver storeSaver;

    @Transactional
    public Long registerStoreWithStoreInfo(final StoreSocialInfoResponse storeSocialInfoResponse) {

        Long socialId = storeSocialInfoResponse.socialId();
        SocialType socialType = storeSocialInfoResponse.socialType();

        Store store = storeSaver.save(null, null, Role.STORE, null, socialId, socialType, null);

        log.info("Store registered with storeId: {}, role: {}", store.getId(), store.getRole());

        return store.getId();
    }
}