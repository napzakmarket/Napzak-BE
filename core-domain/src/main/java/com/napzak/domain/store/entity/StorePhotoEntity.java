package com.napzak.domain.store.entity;

import com.napzak.domain.store.entity.enums.PhotoType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = StorePhotoTableConstants.TABLE_STORE_PHOTO)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StorePhotoEntity {

	@Id
	@Column(name = StoreTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = StorePhotoTableConstants.COLUMN_STORE_ID)
	private Long storeId;

	@Enumerated(EnumType.STRING)
	@Column(name = StorePhotoTableConstants.COLUMN_PHOTO_TYPE)
	private PhotoType photoType;

	@Column(name = StorePhotoTableConstants.COLUMN_PHOTO_URL)
	private String photoUrl;
}
