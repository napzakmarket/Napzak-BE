package com.napzak.domain.store.entity;

import static com.napzak.domain.store.entity.StoreReportTableConstants.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import com.napzak.domain.store.entity.enums.StoreReportApprovalStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_STORE_REPORT)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReportEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_REPORTER_ID, nullable = false)
	private Long reporterId;

	@Column(name = COLUMN_REPORTED_STORE_ID, nullable = false)
	private Long reportedStoreId;

	@Column(name = COLUMN_REPORTED_STORE_NICKNAME, nullable = false)
	private String reportedStoreNickname;

	@Column(name = COLUMN_REPORTED_STORE_PROFILE, nullable = true)
	private String reportedStoreProfile;

	@Column(name = COLUMN_REPORTED_STORE_COVER, nullable = true)
	private String reportedStoreCover;

	@Column(name = COLUMN_REPORTED_STORE_DESCRIPTION, nullable = true)
	private String reportedStoreDescription;

	@Column(name = COLUMN_REPORT_TITLE, nullable = false)
	private String reportTitle;

	@Column(name = COLUMN_REPORT_DESCRIPTION, nullable = false)
	private String reportDescription;

	@Column(name = COLUMN_REPORT_CONTACT, nullable = false)
	private String reportContact;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_REPORT_APPROVAL_STATUS, nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
	private StoreReportApprovalStatus reportApprovalStatus;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public StoreReportEntity(
		Long reporterId,
		Long reportedStoreId,
		String reportedStoreNickname,
		String reportedStoreProfile,
		String reportedStoreCover,
		String reportedStoreDescription,
		String reportTitle,
		String reportDescription,
		String reportContact,
		StoreReportApprovalStatus reportApprovalStatus
	) {
		this.reporterId = reporterId;
		this.reportedStoreId = reportedStoreId;
		this.reportedStoreNickname = reportedStoreNickname;
		this.reportedStoreProfile = reportedStoreProfile;
		this.reportedStoreCover = reportedStoreCover;
		this.reportedStoreDescription = reportedStoreDescription;
		this.reportTitle = reportTitle;
		this.reportDescription = reportDescription;
		this.reportContact = reportContact;
		this.reportApprovalStatus = reportApprovalStatus;
	}

	public static StoreReportEntity create(
		Long reporterId,
		Long reportedStoreId,
		String reportedStoreNickname,
		String reportedStoreProfile,
		String reportedStoreCover,
		String reportedStoreDescription,
		String reportTitle,
		String reportDescription,
		String reportContact
	) {
		return StoreReportEntity.builder()
			.reporterId(reporterId)
			.reportedStoreId(reportedStoreId)
			.reportedStoreNickname(reportedStoreNickname)
			.reportedStoreProfile(reportedStoreProfile)
			.reportedStoreCover(reportedStoreCover)
			.reportedStoreDescription(reportedStoreDescription)
			.reportTitle(reportTitle)
			.reportDescription(reportDescription)
			.reportContact(reportContact)
			.reportApprovalStatus(StoreReportApprovalStatus.PENDING)
			.build();
	}

	public void approve() {
		this.reportApprovalStatus = StoreReportApprovalStatus.APPROVED;
	}

	public void reject() {
		this.reportApprovalStatus = StoreReportApprovalStatus.REJECTED;
	}
}