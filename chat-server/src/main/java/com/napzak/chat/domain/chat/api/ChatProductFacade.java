package com.napzak.chat.domain.chat.api;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.crud.product.ProductUpdater;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatProductFacade {

	private final ProductUpdater productUpdater;

	@Transactional
	public void incrementChatCount(Long productId) {
		productUpdater.incrementChatCount(productId);
	}
}
