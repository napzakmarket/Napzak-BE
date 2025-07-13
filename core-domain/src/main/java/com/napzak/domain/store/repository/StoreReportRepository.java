package com.napzak.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.napzak.domain.store.entity.StoreReportEntity;

public interface StoreReportRepository extends JpaRepository<StoreReportEntity, Long> {

	@Query("SELECT s.reportedStoreCover FROM StoreReportEntity s")
	List<String> findAllReportedStoreCover();

	@Query("SELECT s.reportedStoreProfile FROM StoreReportEntity s")
	List<String> findAllReportedStoreProfile();
}
