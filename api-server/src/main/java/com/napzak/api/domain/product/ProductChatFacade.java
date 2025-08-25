package com.napzak.api.domain.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.napzak.domain.chat.crud.chatmessage.ChatMessageUpdater;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductChatFacade {

	private final ChatParticipantRetriever chatParticipantRetriever;
	private final ChatMessageUpdater chatMessageUpdater;

	public Optional<Long> findCommonRoomIdByStores(Long myStoreId, Long opponentStoreId){
		return chatParticipantRetriever.findCommonRoomIdByStores(myStoreId, opponentStoreId);
	}

	public Long findChatOpponentStoreId(Long roomId, Long myStoreId){
		return chatParticipantRetriever.findOpponentStoreId(roomId, myStoreId);
	}

	public void updateChatMessageProductMetadataIsProductDeletedByProductId(List<Long> productIds, boolean isProductDeleted) {
		if(productIds != null) {
			chatMessageUpdater.updateProductMetadataIsProductDeletedByProductId(productIds, isProductDeleted);
		}
	}

	public void updateChatMessageProductMetadataIsProductDeletedByProductId(Long productId, boolean isProductDeleted) {
		updateChatMessageProductMetadataIsProductDeletedByProductId(java.util.List.of(productId), isProductDeleted);
	}
}
