package com.napzak.chat.domain.chat.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.push.crud.PushDeviceTokenRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatPushFacade {

	private final PushDeviceTokenRetriever pushDeviceTokenRetriever;

	public List<String> findAllowMessageDeviceTokensByStoreId(Long storeId) {
		return pushDeviceTokenRetriever.findAllowMessageDeviceTokensByStoreId(storeId);
	}
}
