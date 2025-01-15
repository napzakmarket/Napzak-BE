package com.napzak.domain.interest.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterestRetriever {

    private final InterestRepository interestRepository;

    public Boolean getIsInterested(Long storeId, Long productId){
        return interestRepository.existsByStoreEntityIdAndProductEntityId(storeId, productId);
    }
}
