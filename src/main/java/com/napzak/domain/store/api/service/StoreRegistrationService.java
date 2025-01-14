package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreEntity;
import com.napzak.domain.store.core.enums.Role;
import com.napzak.domain.store.core.StoreRepository;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRegistrationService {

    private final StoreRepository storeRepository;

    @Transactional
    public Long registerStoreWithStoreInfo(final StoreSocialInfoResponse storeSocialInfoResponse) {

        StoreEntity storeEntity = StoreEntity.create(
                null,
                null,
                Role.STORE,
                null,
                storeSocialInfoResponse.socialId(),
                storeSocialInfoResponse.socialType(),
                null
        );

        storeRepository.save(storeEntity);

        log.info("Store registered with storeId: {}, role: {}", storeEntity.getId(), storeEntity.getRole());

        return storeEntity.getId();
    }
}