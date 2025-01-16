package com.napzak.domain.interest.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InterestRetriever {

    private final InterestRepository interestRepository;


    public Boolean getIsInterested(Long storeId, Long productId){
        return interestRepository.existsByProductIdAndStoreId(storeId, productId);
    }


    public List<Long> getLikedProductIds(List<Long> productIds, Long storeId){
        return interestRepository.findLikedProductIdsByProductIdsAndStoreId(productIds, storeId);
    }
}
