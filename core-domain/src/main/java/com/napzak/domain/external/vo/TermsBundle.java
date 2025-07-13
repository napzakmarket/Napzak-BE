package com.napzak.domain.external.vo;

import java.time.LocalDateTime;

import com.napzak.domain.external.entity.TermsBundleEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TermsBundle {
	private final Long id;
	private final int version;
	private final String description;
	private final LocalDateTime createdAt;
	private final boolean isActive;

	public static TermsBundle fromEntity(TermsBundleEntity termsBundleEntity) {
		return new TermsBundle(
			termsBundleEntity.getId(),
			termsBundleEntity.getVersion(),
			termsBundleEntity.getDescription(),
			termsBundleEntity.getCreatedAt(),
			termsBundleEntity.isActive()
		);
	}
}
