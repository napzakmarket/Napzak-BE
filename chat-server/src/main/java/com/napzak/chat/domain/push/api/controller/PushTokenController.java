package com.napzak.chat.domain.push.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.chat.domain.push.api.code.PushSuccessCode;
import com.napzak.chat.domain.push.api.dto.request.PushTokenRegisterRequest;
import com.napzak.chat.domain.push.api.dto.request.PushTokenSettingUpdateRequest;
import com.napzak.chat.domain.push.api.dto.response.PushSettingResponse;
import com.napzak.chat.domain.push.api.service.PushTokenService;
import com.napzak.common.auth.annotation.AuthorizedRole;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.dto.SuccessResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/push-tokens")
@RequiredArgsConstructor
public class PushTokenController {

	private final PushTokenService pushTokenService;

	@PostMapping
	public ResponseEntity<SuccessResponse<Void>> registerPushToken(
		@RequestBody @Valid PushTokenRegisterRequest request,
		@CurrentMember Long storeId
	) {
		pushTokenService.registerPushToken(
			storeId,
			request.deviceToken(),
			request.platform(),
			request.isEnabled(),
			request.allowMessage()
		);
		return ResponseEntity.ok(SuccessResponse.of(PushSuccessCode.PUSH_TOKEN_CREATE_SUCCESS));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE, Role.REPORTED, Role.BLOCKED})
	@GetMapping("/{deviceToken}/settings")
	public ResponseEntity<SuccessResponse<PushSettingResponse>> getPushSetting(
		@PathVariable String deviceToken,
		@CurrentMember Long storeId
	) {
		boolean allowMessage = pushTokenService.findAllowMessageByStoreIdAndDeviceToken(storeId, deviceToken);
		return ResponseEntity.ok(
			SuccessResponse.of(PushSuccessCode.PUSH_SETTING_RETRIEVE_SUCCESS, PushSettingResponse.of(allowMessage))
		);
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE, Role.REPORTED, Role.BLOCKED})
	@PatchMapping("/{deviceToken}/settings")
	public ResponseEntity<SuccessResponse<Void>> updatePushSetting(
		@PathVariable String deviceToken,
		@RequestBody @Valid PushTokenSettingUpdateRequest request,
		@CurrentMember Long storeId
	) {
		pushTokenService.updateAllowMessage(storeId, deviceToken, request.allowMessage());
		return ResponseEntity.ok(SuccessResponse.of(PushSuccessCode.PUSH_SETTING_UPDATE_SUCCESS));
	}

	@DeleteMapping("/{deviceToken}")
	public ResponseEntity<SuccessResponse<Void>> deletePushToken(
		@PathVariable String deviceToken,
		@CurrentMember Long storeId
	) {
		pushTokenService.deletePushToken(storeId, deviceToken);
		return ResponseEntity.ok(SuccessResponse.of(PushSuccessCode.PUSH_TOKEN_DELETE_SUCCESS));
	}
}
