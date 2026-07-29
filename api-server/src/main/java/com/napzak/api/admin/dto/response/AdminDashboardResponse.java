package com.napzak.api.admin.dto.response;

import java.util.List;

public record AdminDashboardResponse(
	List<AdminUserRow> users,
	List<AdminStoreReportRow> storeReports,
	List<AdminChatRow> chats
) {
}