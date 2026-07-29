package com.napzak.domain.store.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.napzak.domain.store.entity.StoreReportEntity;
import com.napzak.domain.store.entity.enums.StoreReportApprovalStatus;

@Repository
public interface StoreReportRepository extends JpaRepository<StoreReportEntity, Long> {

	List<StoreReportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

	List<StoreReportEntity> findByReportedStoreIdInAndReportApprovalStatus(
		List<Long> reportedStoreIds, StoreReportApprovalStatus reportApprovalStatus);

	@Query("SELECT s.reportedStoreCover FROM StoreReportEntity s")
	List<String> findAllReportedStoreCover();

	@Query("SELECT s.reportedStoreProfile FROM StoreReportEntity s")
	List<String> findAllReportedStoreProfile();
}
