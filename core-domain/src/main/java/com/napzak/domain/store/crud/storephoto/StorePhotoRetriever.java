package com.napzak.domain.store.crud.storephoto;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.entity.enums.PhotoType;
import com.napzak.domain.store.repository.StorePhotoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StorePhotoRetriever {

	private final StorePhotoRepository storePhotoRepository;

	public String getStorePhoto(PhotoType photoType){
		return storePhotoRepository.findByPhotoType(photoType)
			.map(photo -> photo.getPhotoUrl())
			.orElse(null);
	}
}
