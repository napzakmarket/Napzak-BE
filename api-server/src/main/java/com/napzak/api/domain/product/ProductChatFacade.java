package com.napzak.api.domain.product;

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

	public void updateChatMessageProductMetadataIsProductDeleted(Long productId, boolean isProductDeleted) {
		chatMessageUpdater.updateProductMetadataIsProductDeleted(productId, isProductDeleted);
	}
}
