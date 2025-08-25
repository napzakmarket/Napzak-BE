package com.napzak.domain.banner.entity;

import java.time.LocalDateTime;

import com.napzak.domain.banner.entity.enums.BannerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = BannerTableConstants.TABLE_BANNER)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = BannerTableConstants.COLUMN_ID)
	private Long id;

	@Column(name = BannerTableConstants.COLUMN_PHOTO_URL, nullable = false)
	private String photoUrl;

	@Column(name = BannerTableConstants.COLUMN_SEQUENCE, nullable = false)
	private int sequence;

	@Column(name = BannerTableConstants.COLUMN_REDIRECT_URL, nullable = true)
	private String redirectUrl;

	@Column(name = BannerTableConstants.COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = BannerTableConstants.COLUMN_UPDATED_AT, nullable = true)
	private LocalDateTime updatedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = BannerTableConstants.COLUMN_BANNER_TYPE, nullable = false)
	private BannerType bannerType;

	@Column(name = BannerTableConstants.COLUMN_IS_EXTERNAL, nullable = false)
	private Boolean isExternal;

	@Builder
	private BannerEntity(String photoUrl, int sequence, String redirectUrl, LocalDateTime createdAt,
		LocalDateTime updatedAt, BannerType bannerType, Boolean isExternal) {
		this.photoUrl = photoUrl;
		this.sequence = sequence;
		this.redirectUrl = redirectUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.bannerType = bannerType;
		this.isExternal = isExternal;
	}

	public static BannerEntity create(
		final String photoUrl,
		final int sequence,
		final String redirectUrl,
		final BannerType bannerType,
		final Boolean isExternal
	) {
		return BannerEntity.builder()
			.photoUrl(photoUrl)
			.sequence(sequence)
			.redirectUrl(redirectUrl)
			.bannerType(bannerType)
			.isExternal(isExternal)
			.build();
	}

}