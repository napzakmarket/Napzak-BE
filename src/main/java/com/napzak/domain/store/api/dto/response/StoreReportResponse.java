package com.napzak.domain.store.api.dto.response;

public record StoreReportResponse(
	Long reporterId,
	Long reportedStoreId,
	String reportTitle,
	String reportDescription,
	String reportContact
) {
	public static StoreReportResponse of(
		final Long reporterId,
		final Long reportedStoreId,
		final String reportTitle,
		final String reportDescription,
		final String reportContact
	) {
		return new StoreReportResponse(
			reporterId, reportedStoreId, reportTitle, reportDescription, reportContact
		);
	}
}
