package com.napzak.domain.interest.api.service;

import com.napzak.domain.interest.core.InterestRemover;
import com.napzak.domain.interest.core.InterestSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestSaver interestSaver;
    private final InterestRemover interestRemover;

    public void postInterest(Long productId, Long storeId) {
        interestSaver.saveInterest(productId, storeId);
    }

    public void deleteInterest(Long productId, Long storeId) {
        interestRemover.deleteInterest(productId, storeId);
    }

}
