package com.napzak.api.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.amqp.ChatSystemMessageSender;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.store.crud.blacklist.BlacklistSaver;
import com.napzak.domain.store.crud.store.StoreUpdater;
import com.napzak.domain.store.crud.storereport.StoreReportUpdater;
import com.napzak.domain.store.vo.Store;
import com.napzak.api.domain.store.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {
	private final StoreUpdater storeUpdater;
	private final StoreReportUpdater storeReportUpdater;
	private final BlacklistSaver blacklistSaver;
	private final StoreService storeService;
	private final ChatSystemMessageSender chatSystemMessageSender;

	@Transactional
	public void approveReport(Long reportedStoreId, Long reportId){
		storeReportUpdater.approveReport(reportedStoreId, reportId);
		storeUpdater.updateRole(reportedStoreId, Role.REPORTED);

		Store store = storeService.getStore(reportedStoreId);
		if (store.isPhoneVerified()) {
			blacklistSaver.save(reportedStoreId, store.getPhoneNumberEnc(), store.getPhoneNumberHash());
		}
	}

	public void sendReportSystemMessage(List<ChatMessage> messages){
		chatSystemMessageSender.sendSystemMessages(messages);
	}
}
