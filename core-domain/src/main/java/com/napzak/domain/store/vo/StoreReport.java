package com.napzak.domain.store.vo;

import com.napzak.domain.store.entity.StoreReportEntity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class StoreReport {
	private final Long id;
	private final Long reporterId;
	private final Long reportedStoreId;
	private final String reportedStoreNickname;
	private final String reportedStoreProfile;
	private final String reportedStoreCover;
	private final String reportedStoreDescription;
	private final String reportTitle;
	private final String reportDescription;
	private final String reportContact;
	private final LocalDateTime createdAt;

	public StoreReport(
		Long id,
		Long reporterId,
		Long reportedStoreId,
		String reportedStoreNickname,
		String reportedStoreProfile,
		String reportedStoreCover,
		String reportedStoreDescription,
		String reportTitle,
		String reportDescription,
		String reportContact,
		LocalDateTime createdAt
	) {
		this.id = id;
		this.reporterId = reporterId;
		this.reportedStoreId = reportedStoreId;
		this.reportedStoreNickname = reportedStoreNickname;
		this.reportedStoreProfile = reportedStoreProfile;
		this.reportedStoreCover = reportedStoreCover;
		this.reportedStoreDescription = reportedStoreDescription;
		this.reportTitle = reportTitle;
		this.reportDescription = reportDescription;
		this.reportContact = reportContact;
		this.createdAt = createdAt;
	}

	public static StoreReport fromEntity(StoreReportEntity storeReportEntity) {
		return new StoreReport(
			storeReportEntity.getId(),
			storeReportEntity.getReporterId(),
			storeReportEntity.getReportedStoreId(),
			storeReportEntity.getReportedStoreNickname(),
			storeReportEntity.getReportedStoreProfile(),
			storeReportEntity.getReportedStoreCover(),
			storeReportEntity.getReportedStoreDescription(),
			storeReportEntity.getReportTitle(),
			storeReportEntity.getReportDescription(),
			storeReportEntity.getReportContact(),
			storeReportEntity.getCreatedAt()
		);
	}
}
