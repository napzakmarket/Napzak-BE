package com.napzak.api.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.amqp.ChatSystemMessageSender;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.store.crud.store.StoreUpdater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {
	private final StoreUpdater storeUpdater;
	private final ChatSystemMessageSender chatSystemMessageSender;

	@Transactional
	public void approveReport(Long reportedStoreId, List<ChatMessage> messages){
		storeUpdater.updateRole(reportedStoreId, Role.REPORTED);
		chatSystemMessageSender.sendSystemMessages(messages);
	}
}
