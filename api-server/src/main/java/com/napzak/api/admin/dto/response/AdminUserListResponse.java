package com.napzak.api.admin.dto.response;

import java.util.List;

public record AdminUserListResponse(
	List<AdminUserRow> users,
	int currentPage,
	int totalPages,
	boolean hasPrevious,
	boolean hasNext,
	List<Integer> pageNumbers,
	String keyword
) {
}