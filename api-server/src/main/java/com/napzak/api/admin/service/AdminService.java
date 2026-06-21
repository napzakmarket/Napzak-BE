package com.napzak.api.admin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.admin.dto.response.AdminChatListResponse;
import com.napzak.api.admin.dto.response.AdminChatRow;
import com.napzak.api.admin.dto.response.AdminDashboardResponse;
import com.napzak.api.admin.dto.response.AdminStoreReportListResponse;
import com.napzak.api.admin.dto.response.AdminStoreReportRow;
import com.napzak.api.admin.dto.response.AdminUserListResponse;
import com.napzak.api.admin.dto.response.AdminUserRow;
import com.napzak.api.amqp.ChatSystemMessageSender;
import com.napzak.api.domain.store.StoreChatFacade;
import com.napzak.api.domain.store.StoreProductFacade;
import com.napzak.common.auth.redis.LettuceLockRepository;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.util.encryption.PhoneEncryptionUtil;
import com.napzak.domain.admin.code.AdminErrorCode;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageRetriever;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.domain.store.crud.store.StoreUpdater;
import com.napzak.domain.store.crud.storereport.StoreReportRetriever;
import com.napzak.domain.store.crud.storereport.StoreReportUpdater;
import com.napzak.domain.store.vo.Store;
import com.napzak.domain.store.vo.StoreReport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

	private static final int RECENT_LIMIT = 7;
	private static final int CHAT_LIMIT = 30;
	private static final int PAGE_SIZE = 15;
	private static final int WINDOW = 2;
	private static final String LOCK_TYPE = "admin-report";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final StoreRetriever storeRetriever;
	private final StoreReportRetriever storeReportRetriever;
	private final ChatMessageRetriever chatMessageRetriever;
	private final StoreUpdater storeUpdater;
	private final StoreReportUpdater storeReportUpdater;
	private final ChatSystemMessageSender chatSystemMessageSender;
	private final PhoneEncryptionUtil phoneEncryptionUtil;
	private final LettuceLockRepository lettuceLockRepository;
	private final AdminReportProcessor adminReportProcessor;
	private final StoreProductFacade storeProductFacade;
	private final StoreChatFacade storeChatFacade;

	// ===== 대시보드 =====

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
				format(store.getCreatedAt()),
				null
			))
			.toList();
	}

	private List<AdminStoreReportRow> getRecentStoreReports() {
		List<StoreReport> reports = storeReportRetriever.findRecentReports(RECENT_LIMIT);
		return toReportRows(reports);
	}

	private List<AdminChatRow> getRecentChats() {
		return toChatRows(chatMessageRetriever.findRecentTextMessages(CHAT_LIMIT));
	}

	// ===== 유저 목록 =====

	@Transactional(readOnly = true)
	public AdminUserListResponse getUserList(int page) {
		Page<Store> storePage = storeRetriever.findStorePage(page, PAGE_SIZE);

		List<Long> storeIds = storePage.getContent().stream().map(Store::getId).toList();
		// storeId -> 가장 최근 PENDING 신고 id (신고 발행됐지만 승인 안 된 유저 식별용)
		Map<Long, Long> pendingReportIdByStoreId = storeReportRetriever.findPendingReportsByStoreIds(storeIds).stream()
			.collect(Collectors.toMap(
				StoreReport::getReportedStoreId,
				StoreReport::getId,
				(a, b) -> a > b ? a : b));

		List<AdminUserRow> users = storePage.getContent().stream()
			.map(store -> new AdminUserRow(
				store.getId(),
				store.getPhoto(),
				store.getNickname(),
				roleName(store.getRole()),
				format(store.getCreatedAt()),
				pendingReportIdByStoreId.get(store.getId())
			))
			.toList();

		return new AdminUserListResponse(
			users,
			storePage.getNumber(),
			storePage.getTotalPages(),
			storePage.hasPrevious(),
			storePage.hasNext(),
			pageNumbers(storePage.getNumber(), storePage.getTotalPages())
		);
	}

	// ===== 신고 목록 =====

	@Transactional(readOnly = true)
	public AdminStoreReportListResponse getReportList(int page) {
		Page<StoreReport> reportPage = storeReportRetriever.findReportPage(page, PAGE_SIZE);

		return new AdminStoreReportListResponse(
			toReportRows(reportPage.getContent()),
			reportPage.getNumber(),
			reportPage.getTotalPages(),
			reportPage.hasPrevious(),
			reportPage.hasNext(),
			pageNumbers(reportPage.getNumber(), reportPage.getTotalPages())
		);
	}

	// ===== 채팅 목록 =====

	@Transactional(readOnly = true)
	public AdminChatListResponse getChatList(int page, String searchType, String keyword) {
		String trimmed = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
		String type = "nickname".equals(searchType) ? "nickname" : "room";

		Page<ChatMessage> chatPage = findChatPage(page, type, trimmed);

		return new AdminChatListResponse(
			toChatRows(chatPage.getContent()),
			chatPage.getNumber(),
			chatPage.getTotalPages(),
			chatPage.hasPrevious(),
			chatPage.hasNext(),
			pageNumbers(chatPage.getNumber(), chatPage.getTotalPages()),
			type,
			trimmed
		);
	}

	private Page<ChatMessage> findChatPage(int page, String type, String keyword) {
		if (keyword == null) {
			return chatMessageRetriever.findTextMessagePage(page, PAGE_SIZE);
		}
		if ("nickname".equals(type)) {
			List<Long> senderIds = storeRetriever.findStoreIdsByNickname(keyword);
			return senderIds.isEmpty()
				? Page.empty(PageRequest.of(page, PAGE_SIZE))
				: chatMessageRetriever.findTextMessagePageBySenderIds(senderIds, page, PAGE_SIZE);
		}
		// roomId 검색
		Long roomId = parseLongOrNull(keyword);
		return (roomId != null)
			? chatMessageRetriever.findTextMessagePageByRoomId(roomId, page, PAGE_SIZE)
			: Page.empty(PageRequest.of(page, PAGE_SIZE));
	}

	private Long parseLongOrNull(String value) {
		try {
			return Long.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// ===== 신고 / 승인 오케스트레이션 =====

	/**
	 * 신고 발행 - 어드민 페이지의 유저목록 탭에서 '신고-신고 발행만'에 대응
	 */
	public void reportStore(Long storeId) {
		runWithLock(storeId, () -> adminReportProcessor.report(storeId), false);
	}

	/**
	 * 신고 발행 + 승인 - 어드민 페이지의 유저목록 탭에서 '신고-신고 승인까지'에 대응
	 */
	public void reportAndApproveStore(Long storeId) {
		runWithLock(storeId, () -> adminReportProcessor.reportAndApprove(storeId), true);
	}

	/**
	 * 신고 승인
	 * 어드민 페이지의 유저목록 탭에서 '신고 승인'에 대응
	 * 어드민 페이지의 신고내역 탭에서 '신고 승인'에 대응
	 */
	public void approveExistingReport(Long storeId, Long reportId) {
		runWithLock(storeId, () -> adminReportProcessor.approveExisting(storeId, reportId), true);
	}

	private void runWithLock(Long storeId, Runnable criticalSection, boolean withSideEffects) {
		String lockKey = LOCK_TYPE + ":" + storeId;
		Boolean acquired = lettuceLockRepository.lock(lockKey, LOCK_TYPE);
		if (!Boolean.TRUE.equals(acquired)) {
			throw new NapzakException(AdminErrorCode.REPORT_PROCESSING_CONFLICT);
		}
		try {
			criticalSection.run();
			if (withSideEffects) {
				// 신고 승인의 경우, 유저 ROLE 변경, 관련 상품 isVisible 업데이트, 채팅메시지 브로드캐스트 진행
				applyReportSideEffects(storeId);
			}
		} finally {
			lettuceLockRepository.unlock(lockKey);
		}
	}

	private void applyReportSideEffects(Long storeId) {
		storeProductFacade.updateProductIsVisibleByStoreId(storeId);

		List<Long> productIds = storeProductFacade.getProductIdsByStoreId(storeId);
		storeChatFacade.updateChatMessageProductMetadataIsProductDeletedByProductId(productIds, true);

		List<ChatMessage> messages = storeChatFacade.broadcastSystemMessage(storeId, SystemMessageType.REPORTED);
		sendReportSystemMessage(messages);
	}

	// ===== 기타 ADMIN 로직 =====

	@Transactional
	public void approveReport(Long reportedStoreId, Long reportId) {
		storeReportUpdater.approveReport(reportedStoreId, reportId);
		storeUpdater.updateRole(reportedStoreId, Role.REPORTED);
	}

	public void sendReportSystemMessage(List<ChatMessage> messages) {
		chatSystemMessageSender.sendSystemMessages(messages);
	}

	public String decryptPhoneNumber(String phoneNumberEnc) {
		return phoneEncryptionUtil.decrypt(phoneNumberEnc);
	}

	// ===== 헬퍼 =====

	private List<AdminStoreReportRow> toReportRows(List<StoreReport> reports) {
		List<Long> reportedStoreIds = reports.stream()
			.map(StoreReport::getReportedStoreId).distinct().toList();
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

	private List<AdminChatRow> toChatRows(List<ChatMessage> messages) {
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
					message.getRoomId(),
					message.getSenderId(),
					sender != null ? sender.getPhoto() : null,
					sender != null ? sender.getNickname() : "-",
					message.getContent(),
					format(message.getCreatedAt())
				);
			})
			.toList();
	}

	private List<Integer> pageNumbers(int current, int totalPages) {
		if (totalPages == 0) {
			return List.of();
		}
		return IntStream.rangeClosed(
				Math.max(0, current - WINDOW),
				Math.min(totalPages - 1, current + WINDOW))
			.boxed()
			.toList();
	}

	private String roleName(Role role) {
		return role != null ? role.name() : "-";
	}

	private String format(LocalDateTime dateTime) {
		return dateTime != null ? dateTime.format(DATE_FORMAT) : "-";
	}
}
