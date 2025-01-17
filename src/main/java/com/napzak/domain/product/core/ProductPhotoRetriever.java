package com.napzak.domain.product.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.core.entity.ProductPhotoEntity;
import com.napzak.domain.product.core.vo.ProductPhoto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPhotoRetriever {

	private final ProductPhotoRepository productPhotoRepository;

	@Transactional(readOnly = true)
	public Map<Long, String> getFirstProductPhotos(List<Long> productIds) {
		List<Object[]> results = productPhotoRepository.findFirstPhotosByProductIds(productIds);

		// 결과를 Map으로 변환 (상품 ID -> 첫 번째 사진 URL)
		return results.stream()
			.collect(Collectors.toMap(
				row -> (Long) row[0],   // productId
				row -> (String) row[1] // photoUrl
			));
	}

	@Transactional(readOnly = true)
	public List<ProductPhoto> getProductPhotosByProductId(Long productId) {
		List <ProductPhotoEntity> productPhotoEntityList = productPhotoRepository.findAllByProductEntityOrderBySequenceAsc(productId);
		return productPhotoEntityList.stream().map(ProductPhoto::fromEntity).toList();
	}
}
