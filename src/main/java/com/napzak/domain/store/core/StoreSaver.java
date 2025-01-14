package com.napzak.domain.store.core;

import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreSaver {
    private final StoreRepository storeRepository;

    @Transactional
    public StoreEntity save(
            final String nickname,
            final String phoneNumber,
            final Role role,
            final String description,
            final Long socialId,
            final SocialType socialType,
            final String photo
            ){
        final StoreEntity storeEntity = StoreEntity.create(nickname, phoneNumber, role, description, socialId, socialType, photo);
        storeRepository.save(storeEntity);

        return storeEntity;
    }
}
