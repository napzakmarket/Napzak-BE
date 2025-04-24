package com.napzak.domain.product.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.entity.ProductPhotoEntity;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class ProductPhotoUpdater {

	private final ProductPhotoRepository productPhotoRepository;

	public void updateProductPhoto(Long photoId, String photoUrl, int sequence) {
		ProductPhotoEntity productPhoto = productPhotoRepository.findById(photoId)
			.orElseThrow(() -> new NapzakException(ProductErrorCode.PRODUCT_PHOTO_NOT_FOUND));

		productPhoto.update(photoUrl, sequence);
		productPhotoRepository.save(productPhoto);
	}

}
