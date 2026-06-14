package com.napzak.api.admin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.admin.dto.response.AdminChatRow;
import com.napzak.api.admin.dto.response.AdminDashboardResponse;
import com.napzak.api.admin.dto.response.AdminStoreReportRow;
import com.napzak.api.admin.dto.response.AdminUserRow;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageRetriever;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.domain.store.crud.storereport.StoreReportRetriever;
import com.napzak.domain.store.vo.Store;
import com.napzak.domain.store.vo.StoreReport;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

	private static final int RECENT_LIMIT = 7;
	private static final int CHAT_LIMIT = 30;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final StoreRetriever storeRetriever;
	private final StoreReportRetriever storeReportRetriever;
	private final ChatMessageRetriever chatMessageRetriever;

	@Transactional(readOnly = true)
	public AdminDashboardResponse getDashboard() {
		return new AdminDashboardResponse(
			getRecentUsers(),
			getRecentStoreReports(),
			getRecentChats()
		);
	}

	private List<AdminUserRow> getRecentUsers() {
		return storeRetriever.findRecentStores(RECENT_LIMIT).stream()
			.map(store -> new AdminUserRow(
				store.getId(),
				store.getPhoto(),
				store.getNickname(),
				roleName(store.getRole()),
				format(store.getCreatedAt())
			))
			.toList();
	}

	private List<AdminStoreReportRow> getRecentStoreReports() {
		List<StoreReport> reports = storeReportRetriever.findRecentReports(RECENT_LIMIT);

		List<Long> reportedStoreIds = reports.stream().map(StoreReport::getReportedStoreId).distinct().toList();
		Map<Long, Role> roleByStoreId = storeRetriever.findStoresByStoreIds(reportedStoreIds).stream()
			.collect(Collectors.toMap(Store::getId, Store::getRole, (a, b) -> a));

		return reports.stream()
			.map(report -> new AdminStoreReportRow(
				report.getId(),
				report.getReportedStoreId(),
				report.getReportedStoreProfile(),
				report.getReportedStoreNickname(),
				roleName(roleByStoreId.get(report.getReportedStoreId())),
				report.getReportApprovalStatus() != null ? report.getReportApprovalStatus().name() : "-",
				format(report.getCreatedAt())
			))
			.toList();
	}

	private List<AdminChatRow> getRecentChats() {
		List<ChatMessage> messages = chatMessageRetriever.findRecentTextMessages(CHAT_LIMIT);

		List<Long> senderIds = messages.stream()
			.map(ChatMessage::getSenderId)
			.filter(Objects::nonNull)
			.distinct()
			.toList();
		Map<Long, Store> storeBySenderId = storeRetriever.findStoresByStoreIds(senderIds).stream()
			.collect(Collectors.toMap(Store::getId, Function.identity(), (a, b) -> a));

		return messages.stream()
			.map((ChatMessage message) -> {
				Store sender = storeBySenderId.get(message.getSenderId());
				return new AdminChatRow(
					message.getId(),
					message.getSenderId(),
					sender != null ? sender.getPhoto() : null,
					sender != null ? sender.getNickname() : "-",
					message.getContent(),
					format(message.getCreatedAt())
				);
			})
			.toList();
	}

	private String roleName(Role role) {
		return role != null ? role.name() : "-";
	}

	private String format(LocalDateTime dateTime) {
		return dateTime != null ? dateTime.format(DATE_FORMAT) : "-";
	}
}
