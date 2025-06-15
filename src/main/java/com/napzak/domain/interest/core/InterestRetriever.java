package com.napzak.domain.interest.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.interest.core.entity.InterestEntity;
import com.napzak.domain.interest.core.vo.Interest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InterestRetriever {
	private final InterestRepository interestRepository;

	@Transactional(readOnly = true)
	public boolean getIsInterested(Long productId, Long storeId) {
		return interestRepository.existsByProductIdAndStoreId(productId, storeId);
	}

	@Transactional(readOnly = true)
	public Map<Long, Boolean> areProductsInterested(List<Long> productIds, Long storeId) {
		// Repository에서 좋아요된 상품 ID 목록 조회
		List<Long> likedProductIds = interestRepository.findLikedProductIdsByStore(productIds, storeId);

		// 상품 ID 리스트와 조회된 좋아요 ID를 매핑하여 Boolean 결과 생성
		return productIds.stream()
			.collect(Collectors.toMap(
				productId -> productId,          // 상품 ID
				likedProductIds::contains        // 좋아요 여부
			));
	}

	@Transactional(readOnly = true)
	public Interest retrieveInterestByProductIdAndStoreId(Long productId, Long storeId) {
		InterestEntity interestEntity = interestRepository.findByProductIdAndStoreId(productId, storeId);

		return Interest.fromEntity(interestEntity);
	}
}
