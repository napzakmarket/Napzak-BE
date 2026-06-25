package com.napzak.api.admin.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.NapzakException;
import com.napzak.domain.admin.code.AdminErrorCode;
import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.domain.store.crud.store.StoreUpdater;
import com.napzak.domain.store.crud.storereport.StoreReportSaver;
import com.napzak.domain.store.crud.storereport.StoreReportUpdater;
import com.napzak.domain.store.vo.Store;

import lombok.RequiredArgsConstructor;

/**
 * 어드민의 "신고 + 신고 승인"을 하나의 트랜잭션으로 처리하는 임계영역 빈
 * 락은 호출자(AdminService)가 잡으며, 이 메서드는 락 안에서 호출됨
 */
@Component
@RequiredArgsConstructor
public class AdminReportProcessor {

	private static final Long ADMIN_REPORTER_ID = 0L;
	private static final String REPORT_TITLE = "admin에 의해 신고 & 신고 승인되었습니다";
	private static final String ISSUE_ONLY_REPORT_TITLE = "admin에 의해 신고되었습니다";
	private static final String REPORT_DESCRIPTION = "-";
	private static final String REPORT_CONTACT = "0";

	private final StoreRetriever storeRetriever;
	private final StoreReportSaver storeReportSaver;
	private final StoreReportUpdater storeReportUpdater;
	private final StoreUpdater storeUpdater;

	@Transactional
	public void reportAndApprove(Long storeId) {
		// 유저 조회 후 role이 STORE인지 재확인
		Store store = storeRetriever.findStoreByStoreId(storeId);
		if (store.getRole() != Role.STORE) {
			throw new NapzakException(AdminErrorCode.STORE_ROLE_REQUIRED);
		}

		//  미리 설정한 템플릿으로 신고 생성
		Long reportId = storeReportSaver.save(
			ADMIN_REPORTER_ID, store, REPORT_TITLE, REPORT_DESCRIPTION, REPORT_CONTACT);

		// 신고 승인 + role REPORTED 전환
		storeReportUpdater.approveReport(storeId, reportId);
		storeUpdater.updateRole(storeId, Role.REPORTED);
	}

	@Transactional
	public void report(Long storeId) {
		Store store = storeRetriever.findStoreByStoreId(storeId);
		if (store.getRole() != Role.STORE) {
			throw new NapzakException(AdminErrorCode.STORE_ROLE_REQUIRED);
		}
		storeReportSaver.save(
			ADMIN_REPORTER_ID, store, ISSUE_ONLY_REPORT_TITLE, REPORT_DESCRIPTION, REPORT_CONTACT);
	}

	@Transactional
	public void approveExisting(Long storeId, Long reportId) {
		storeReportUpdater.approveReport(storeId, reportId);
		storeUpdater.updateRole(storeId, Role.REPORTED);
	}
}