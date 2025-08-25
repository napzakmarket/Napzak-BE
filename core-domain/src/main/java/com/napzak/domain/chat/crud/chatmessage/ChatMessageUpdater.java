package com.napzak.domain.chat.crud.chatmessage;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.chat.repository.ChatMessageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageUpdater {

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public void updateProductMetadataIsProductDeletedByProductId(List<Long> productIds, boolean isProductDeleted) {
		chatMessageRepository.updateProductMetadataIsProductDeletedByProductId(productIds, isProductDeleted);
	}
}
