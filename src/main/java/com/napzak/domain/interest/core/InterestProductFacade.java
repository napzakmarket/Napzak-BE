package com.napzak.domain.interest.core;

import com.napzak.domain.product.core.ProductRepository;
import com.napzak.domain.product.core.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterestProductFacade {

    private final ProductRepository productRepository;

    public void incrementInterestCount(Long productId) {
        productRepository.incrementInterestCount(productId);
    }

    public void decrementInterestCount(Long productId) {
        productRepository.decrementInterestCount(productId);
    }
}
