package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.StoreSaver;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.StoreRepository;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import org.apache.catalina.Store;
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

        StoreEntity storeEntity = storeSaver.save(null, null, Role.STORE, null, storeSocialInfoResponse.socialId(), storeSocialInfoResponse.socialType(), null);

        log.info("Store registered with storeId: {}, role: {}", storeEntity.getId(), storeEntity.getRole());

        return storeEntity.getId();
    }
}