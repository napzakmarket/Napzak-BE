package com.napzak.domain.store.crud.storereport;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.entity.enums.StoreReportApprovalStatus;
import com.napzak.domain.store.repository.StoreReportRepository;
import com.napzak.domain.store.vo.StoreReport;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreReportRetriever {

	private final StoreReportRepository storeReportRepository;

	@Transactional(readOnly = true)
	public List<StoreReport> findRecentReports(int limit) {
		return storeReportRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit))
			.stream().map(StoreReport::fromEntity).toList();
	}
	@Transactional(readOnly = true)
	public List<StoreReport> findPendingReportsByStoreIds(List<Long> storeIds) {
		if (storeIds.isEmpty()) {
			return List.of();
		}
		return storeReportRepository
			.findByReportedStoreIdInAndReportApprovalStatus(storeIds, StoreReportApprovalStatus.PENDING)
			.stream().map(StoreReport::fromEntity).toList();
	}
}
