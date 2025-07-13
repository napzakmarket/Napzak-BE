package com.napzak.domain.store.crud.store;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.entity.StoreEntity;
import com.napzak.domain.store.repository.StoreRepository;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.auth.client.enums.SocialType;
import com.napzak.domain.store.vo.Store;

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
		final String socialId,
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
