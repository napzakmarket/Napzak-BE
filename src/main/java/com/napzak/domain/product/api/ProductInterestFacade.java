package com.napzak.domain.product.api;

import com.napzak.domain.interest.core.InterestRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductInterestFacade {

    private final InterestRetriever interestRetriever;

    public Boolean getIsInterested(Long productId, Long storeId){
        return interestRetriever.getIsInterested(productId, storeId);
    }
}
