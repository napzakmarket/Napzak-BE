package com.napzak.domain.interest.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InterestRemover {

    private final InterestRepository interestRepository;

    @Transactional
    public void deleteInterest(Long productId, Long storeId) {
        interestRepository.deleteByProductIdAndStoreId(productId, storeId);
    }
}
