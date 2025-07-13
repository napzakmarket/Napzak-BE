package com.napzak.domain.product.crud.productphoto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.repository.ProductPhotoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPhotoRemover {

	private final ProductPhotoRepository productPhotoRepository;

	@Transactional
	public void deleteAllByProductPhotoIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			productPhotoRepository.deleteAllByProductPhotoIds(ids);
		}
	}

	@Transactional
	public void deleteByProductId(Long productId) {
		productPhotoRepository.deleteAllByProductId(productId);
	}
}
