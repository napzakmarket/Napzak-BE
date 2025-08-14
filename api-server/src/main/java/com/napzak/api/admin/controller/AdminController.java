package com.napzak.api.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.admin.code.AdminSuccessCode;
import com.napzak.api.admin.service.AdminService;
import com.napzak.api.domain.store.StoreChatFacade;
import com.napzak.api.domain.store.StoreProductFacade;
import com.napzak.common.auth.annotation.AuthorizedRole;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final StoreChatFacade storeChatFacade;
	private final StoreProductFacade storeProductFacade;

	@AuthorizedRole({Role.ADMIN})
	@PatchMapping("/report-approval/{storeId}")
	public ResponseEntity<SuccessResponse<Void>> reportApproval(
		@CurrentMember final Long currentStoreId,
		@PathVariable("storeId") Long reportedStoreId
	){
		List<ChatMessage> messages = storeChatFacade.broadcastSystemMessage(reportedStoreId, SystemMessageType.REPORTED);
		storeProductFacade.updateProductIsVisibleByStoreId(reportedStoreId);
		adminService.approveReport(reportedStoreId, messages);
		return ResponseEntity.ok(SuccessResponse.of(AdminSuccessCode.STORE_REPORT_APPROVE_SUCCESS));
	}

}
