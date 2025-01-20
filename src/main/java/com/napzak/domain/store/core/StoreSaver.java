package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreSaver {
	private final StoreRepository storeRepository;

	@Transactional
	public Store save(
		final String nickname,
		final String phoneNumber,
		final Role role,
		final String description,
		final Long socialId,
		final SocialType socialType,
		final String photo,
		final String cover
	) {
		final StoreEntity storeEntity = StoreEntity.create(nickname, phoneNumber, role, description, socialId,
			socialType, photo, cover);

		storeRepository.save(storeEntity);

		return Store.fromEntity(storeEntity);
	}
}
