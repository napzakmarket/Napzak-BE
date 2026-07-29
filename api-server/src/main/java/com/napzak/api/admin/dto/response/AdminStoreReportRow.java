package com.napzak.api.admin.dto.response;

public record AdminStoreReportRow(
	Long id,
	Long reportedStoreId,
	String profile,
	String nickname,
	String role,
	String approvalStatus,
	String createdAt
) {
}