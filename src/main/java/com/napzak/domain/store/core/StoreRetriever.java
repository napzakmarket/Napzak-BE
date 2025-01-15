package com.napzak.domain.store.core;

import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.common.exception.NapzakException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StoreRetriever {

    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public Store findBySocialTypeAndSocialId(Long socialId, SocialType socialType){
        StoreEntity storeEntity = storeRepository.findBySocialTypeAndSocialId(socialId, socialType)
                .orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
        return Store.fromEntity(storeEntity);
    }

    @Transactional(readOnly = true)
    public Store findStoreByStoreId(Long StoreId){
        StoreEntity storeEntity = storeRepository.findById(StoreId)
                .orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
        return Store.fromEntity(storeEntity);
    }

    @Transactional(readOnly = true)
    public boolean checkStoreExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType){
        return storeRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
    }
}
