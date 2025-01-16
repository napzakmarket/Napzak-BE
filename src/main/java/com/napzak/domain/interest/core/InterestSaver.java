package com.napzak.domain.interest.core;

import com.napzak.domain.interest.core.entity.InterestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InterestSaver {

    private final InterestRepository interestRepository;

    @Transactional
    public void saveInterest(final Long productId, final Long storeId) {
        final InterestEntity interestEntity = InterestEntity.create(productId, storeId);
        interestRepository.save(interestEntity);
    }

}
