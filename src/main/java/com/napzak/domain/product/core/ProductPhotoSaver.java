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
public class ProductPhotoSaver {
	private final ProductPhotoRepository productPhotoRepository;

	public List<ProductPhoto> saveAll(final Long productId, final Map<Integer, String> photoData) {
		List<ProductPhotoEntity> productPhotoEntities = photoData.entrySet().stream()
			.map(entry -> ProductPhotoEntity.create(productId, entry.getValue(), entry.getKey()))
			.collect(Collectors.toList());

		List<ProductPhotoEntity> savedProductPhotoEntities = productPhotoRepository.saveAll(productPhotoEntities);

		return savedProductPhotoEntities.stream()
			.map(ProductPhoto::fromEntity)
			.collect(Collectors.toList());
	}
}
