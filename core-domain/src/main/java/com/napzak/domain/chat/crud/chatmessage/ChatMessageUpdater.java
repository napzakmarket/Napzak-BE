package com.napzak.domain.chat.crud.chatmessage;

import org.springframework.stereotype.Component;

import com.napzak.domain.chat.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageUpdater {

	private final ChatMessageRepository chatMessageRepository;

	public void updateProductMetadataIsProductDeleted(Long productId, boolean isProductDeleted) {
		chatMessageRepository.updateProductMetadataIsProductDeleted(productId, isProductDeleted);
	}
}
