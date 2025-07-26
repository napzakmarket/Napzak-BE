package com.napzak.chat.domain.push.api.controller;

import java.util.Map;

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
import com.napzak.chat.domain.push.api.dto.request.PushTestRequest;
import com.napzak.chat.domain.push.api.dto.request.PushTokenRegisterRequest;
import com.napzak.chat.domain.push.api.dto.request.PushTokenSettingUpdateRequest;
import com.napzak.chat.domain.push.api.dto.response.PushSettingResponse;
import com.napzak.chat.domain.push.api.service.PushTokenService;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.domain.push.code.PushErrorCode;
import com.napzak.domain.push.util.FcmPushSender;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/push-tokens")
@RequiredArgsConstructor
public class PushTokenController {

	private final PushTokenService pushTokenService;
	//테스트용
	private final FcmPushSender fcmPushSender;

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

	@PostMapping("/test")
	public ResponseEntity<SuccessResponse<Void>> sendTestPush(
		@CurrentMember Long storeId,
		@RequestBody @Valid PushTestRequest request
	) {
		String body = switch (request.messageType()) {
			case TEXT -> request.notification().body();
			case IMAGE -> "(사진)";
			default -> throw new NapzakException(PushErrorCode.TYPE_NOT_SERVICED);
		};

		try {
			fcmPushSender.sendMessage(
				request.opponentId(),
				request.token(),
				request.notification().title(),
				body,
				Map.of("type", "chat", "roomId", String.valueOf(request.data().roomId()))
			);
		} catch (Exception e) { throw new NapzakException(PushErrorCode.PUSH_DELIVERY_FAILED); }

		return ResponseEntity.ok(SuccessResponse.of(PushSuccessCode.PUSH_TEST_SUCCESS));
	}
}
