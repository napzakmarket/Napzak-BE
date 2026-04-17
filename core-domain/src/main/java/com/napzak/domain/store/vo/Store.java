package com.napzak.domain.store.vo;

import java.time.LocalDateTime;

import com.napzak.common.auth.client.enums.SocialType;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.store.entity.StoreEntity;

import lombok.Getter;

@Getter
public class Store {
	private final Long id;
	private final String nickname;
	private final String phoneNumberEnc;
	private final boolean phoneVerified;
	private final LocalDateTime verifiedAt;
	private final String email;
	private final String photo;
	private final String cover;
	private final Role role;
	private final String description;
	private final LocalDateTime createdAt;
	private final LocalDateTime deletedAt;
	private final String socialId;
	private final SocialType socialType;

	public Store(
		Long id,
		String nickname,
		String phoneNumberEnc,
		boolean phoneVerified,
		LocalDateTime verifiedAt,
		String email,
		String photo,
		String cover,
		Role role,
		String description,
		LocalDateTime createdAt,
		LocalDateTime deletedAt,
		String socialId,
		SocialType socialType
	) {
		this.id = id;
		this.nickname = nickname;
		this.phoneNumberEnc = phoneNumberEnc;
		this.phoneVerified = phoneVerified;
		this.verifiedAt = verifiedAt;
		this.email = email;
		this.photo = photo;
		this.cover = cover;
		this.role = role;
		this.description = description;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.socialId = socialId;
		this.socialType = socialType;
	}

	public static Store fromEntity(StoreEntity storeEntity) {
		return new Store(
			storeEntity.getId(),
			storeEntity.getNickname(),
			storeEntity.getPhoneNumberEnc(),
			storeEntity.isPhoneVerified(),
			storeEntity.getVerifiedAt(),
			storeEntity.getEmail(),
			storeEntity.getPhoto(),
			storeEntity.getCover(),
			storeEntity.getRole(),
			storeEntity.getDescription(),
			storeEntity.getCreatedAt(),
			storeEntity.getDeletedAt(),
			storeEntity.getSocialId(),
			storeEntity.getSocialType()
		);
	}
}