package com.napzak.api.admin.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.admin.dto.response.AdminLoginResponse;
import com.napzak.common.auth.jwt.provider.JwtTokenProvider;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.auth.security.AdminAuthentication;
import com.napzak.common.exception.NapzakException;
import com.napzak.domain.admin.code.AdminErrorCode;
import com.napzak.domain.admin.crud.AdminRetriever;
import com.napzak.domain.admin.vo.Admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLoginService {

	private final AdminRetriever adminRetriever;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 어드민 loginId/password 로그인 후 Access Token을 발급한다. (Refresh Token은 발급하지 않는다)
	 * 어드민 PK를 JWT의 storeId 클레임 자리에 담고 role을 ADMIN으로 발급한다.
	 */
	@Transactional
	public AdminLoginResponse login(final String loginId, final String rawPassword) {
		Admin admin = adminRetriever.findByLoginId(loginId);

		if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
			throw new NapzakException(AdminErrorCode.INVALID_PASSWORD);
		}

		Collection<GrantedAuthority> authorities = List.of(Role.ADMIN.toGrantedAuthority());
		AdminAuthentication authentication = new AdminAuthentication(admin.getId(), null, authorities);

		String accessToken = jwtTokenProvider.issueAccessToken(authentication);

		log.info("Admin login success for adminId: {}", admin.getId());

		return AdminLoginResponse.of(accessToken);
	}
}
