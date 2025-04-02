package com.napzak.domain.store.core.vo;

import com.napzak.domain.store.core.entity.StorePhotoEntity;
import com.napzak.domain.store.core.entity.enums.PhotoType;

import lombok.Getter;

@Getter
public class StorePhoto {
	private final Long id;
	private final Long storeId;
	private final PhotoType photoType;
	private final String photoUrl;

	public StorePhoto(Long id, Long storeId, PhotoType photoType, String photoUrl) {
		this.id = id;
		this.storeId = storeId;
		this.photoType = photoType;
		this.photoUrl = photoUrl;
	}

	public static StorePhoto fromEntity(StorePhotoEntity storePhotoEntity) {
		return new StorePhoto(
			storePhotoEntity.getId(),
			storePhotoEntity.getStoreId(),
			storePhotoEntity.getPhotoType(),
			storePhotoEntity.getPhotoUrl()
		);
	}

}
