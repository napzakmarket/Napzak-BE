package com.napzak.api.admin.dto.response;

import java.util.List;

public record AdminChatListResponse(
	List<AdminChatRow> chats,
	int currentPage,
	int totalPages,
	boolean hasPrevious,
	boolean hasNext,
	List<Integer> pageNumbers,
	String searchType,
	String keyword
) {
}
