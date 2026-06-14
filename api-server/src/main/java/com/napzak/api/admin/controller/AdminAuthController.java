package com.napzak.api.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.admin.code.AdminSuccessCode;
import com.napzak.api.admin.dto.request.AdminLoginRequest;
import com.napzak.api.admin.dto.response.AdminLoginResponse;
import com.napzak.api.admin.service.AdminLoginService;
import com.napzak.common.exception.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminAuthController {

	private final AdminLoginService adminLoginService;

	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<AdminLoginResponse>> login(
		@RequestBody AdminLoginRequest request
	) {
		AdminLoginResponse response = adminLoginService.login(request.loginId(), request.password());
		return ResponseEntity.ok(SuccessResponse.of(AdminSuccessCode.LOGIN_SUCCESS, response));
	}
}
