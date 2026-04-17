package com.napzak.domain.store.entity;

import java.time.LocalDateTime;

import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.auth.client.enums.SocialType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	name = StoreTableConstants.TABLE_STORE,
	indexes = {
		@Index(name = "uk_store_phone_number_hash", columnList = StoreTableConstants.COLUMN_PHONE_NUMBER_HASH, unique = true)
	}
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreEntity {

	@Column(name = StoreTableConstants.COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Id
	@Column(name = StoreTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = StoreTableConstants.COLUMN_NICKNAME, nullable = true)
	private String nickname;

	@Column(name = StoreTableConstants.COLUMN_PHONE_NUMBER_ENC, nullable = true)
	private String phoneNumberEnc;

	@Column(name = StoreTableConstants.COLUMN_PHONE_NUMBER_HASH, nullable = true)
	private String phoneNumberHash;

	@Column(name = StoreTableConstants.COLUMN_PHONE_VERIFIED, nullable = false)
	private boolean phoneVerified;

	@Column(name = StoreTableConstants.COLUMN_VERIFIED_AT, nullable = true)
	private LocalDateTime verifiedAt;

	@Column(name = StoreTableConstants.COLUMN_EMAIL, nullable = true)
	private String email;

	@Column(name = StoreTableConstants.COLUMN_PHOTO, nullable = true)
	private String photo;

	@Column(name = StoreTableConstants.COLUMN_COVER, nullable = true)
	private String cover;

	@Enumerated(EnumType.STRING)
	@Column(name = StoreTableConstants.COLUMN_ROLE, nullable = false, columnDefinition = "varchar(10) default 'STORE'")
	private Role role;

	@Column(name = StoreTableConstants.COLUMN_DESCRIPTION, nullable = true, columnDefinition = "varchar(58)")
	private String description;

	@Column(name = StoreTableConstants.COLUMN_DELETED_AT, nullable = true)
	private LocalDateTime deletedAt;

	@Column(name = StoreTableConstants.COLUMN_SOCIAL_ID, nullable = true)
	private String socialId;

	@Enumerated(EnumType.STRING)
	@Column(name = StoreTableConstants.COLUMN_SOCIAL_TYPE, nullable = true)
	private SocialType socialType;

	@Builder
	private StoreEntity(
		String nickname,
		boolean phoneVerified,
		LocalDateTime verifiedAt,
		String email,
		Role role,
		String description,
		String socialId,
		SocialType socialType,
		String photo,
		String cover
	) {
		this.nickname = nickname;
		this.phoneVerified = phoneVerified;
		this.verifiedAt = verifiedAt;
		this.email = email;
		this.photo = photo;
		this.cover = cover;
		this.role = role;
		this.description = description;
		this.socialId = socialId;
		this.socialType = socialType;
	}

	public static StoreEntity create(
		final String nickname,
		final String email,
		final Role role,
		final String description,
		final String socialId,
		final SocialType socialType,
		final String photo,
		final String cover
	) {
		return StoreEntity.builder()
			.nickname(nickname)
			.phoneVerified(false)
			.verifiedAt(null)
			.email(email)
			.role(role)
			.description(description)
			.socialId(socialId)
			.socialType(socialType)
			.photo(photo)
			.cover(cover)
			.build();
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void withdraw() {
		this.role = Role.WITHDRAWN;
		this.description = null;
		this.socialId = null;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void verifyAndUpdatePhone(String phoneNumberEnc, String phoneNumberHash) {
		this.phoneNumberEnc = phoneNumberEnc;
		this.phoneNumberHash = phoneNumberHash;
		this.phoneVerified = true;
		this.verifiedAt = LocalDateTime.now();
	}

	public void clearPhoneVerification() {
		this.phoneNumberEnc = null;
		this.phoneNumberHash = null;
		this.phoneVerified = false;
		this.verifiedAt = null;
	}
}
