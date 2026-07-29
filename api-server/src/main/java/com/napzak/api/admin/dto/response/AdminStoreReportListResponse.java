package com.napzak.api.admin.dto.response;

import java.util.List;

public record AdminStoreReportListResponse(
	List<AdminStoreReportRow> reports,
	int currentPage,
	int totalPages,
	boolean hasPrevious,
	boolean hasNext,
	List<Integer> pageNumbers
) {
}