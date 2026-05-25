package com.napzak.domain.store.crud.storereport;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.store.code.StoreErrorCode;
import com.napzak.domain.store.entity.StoreReportEntity;
import com.napzak.domain.store.entity.enums.StoreReportApprovalStatus;
import com.napzak.domain.store.repository.StoreReportRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreReportUpdater {

	private final StoreReportRepository storeReportRepository;

	@Transactional
	public void approveReport(Long reportedStoreId, Long reportId) {
		StoreReportEntity entity = storeReportRepository.findById(reportId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_REPORT_NOT_FOUND));
		if (!entity.getReportedStoreId().equals(reportedStoreId)) {
			throw new NapzakException(StoreErrorCode.INVALID_STORE_REPORT_APPROVAL_REQUEST);
		}
		if (entity.getReportApprovalStatus() != StoreReportApprovalStatus.PENDING) {
			throw new NapzakException(StoreErrorCode.STORE_REPORT_ALREADY_PROCESSED);
		}
		entity.approve();
	}
}
