package com.napzak.domain.store.crud.storeblock;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.repository.StoreBlockRepository;
import com.napzak.domain.store.vo.BlockStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreBlockRetriever {

	private final StoreBlockRepository storeBlockRepository;

	@Transactional(readOnly = true)
	public boolean isOpponentStoreBlocked(Long myStoreId, Long opponentStoreId) {
		return storeBlockRepository.existsByBlockerStoreIdAndBlockedStoreId(myStoreId, opponentStoreId);
	}

	@Transactional(readOnly = true)
	public BlockStatus getBlockStatus(Long myStoreId, Long opponentStoreId) {
		if (Objects.equals(myStoreId, opponentStoreId)) {
			return new BlockStatus(false, false);
		}
		boolean isOpponentStoreBlocked  =
			storeBlockRepository.existsByBlockerStoreIdAndBlockedStoreId(myStoreId, opponentStoreId);
		boolean isChatBlocked = storeBlockRepository.existsByBlockerStoreIdAndBlockedStoreId(opponentStoreId, myStoreId);

		return new BlockStatus(isOpponentStoreBlocked, isChatBlocked);
	}
}
