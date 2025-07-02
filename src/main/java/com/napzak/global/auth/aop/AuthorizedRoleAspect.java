package com.napzak.global.auth.aop;

import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.vo.StoreSession;
import com.napzak.global.auth.annotation.AuthorizedRole;
import com.napzak.global.auth.context.StoreSessionContextHolder;
import com.napzak.global.auth.role.RoleProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizedRoleAspect {

	private final List<RoleProvider> roleProviders;

	@Before("@annotation(com.napzak.global.auth.annotation.AuthorizedRole) || @within(com.napzak.global.auth.annotation.AuthorizedRole)")
	public void checkRole(JoinPoint joinPoint) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new AccessDeniedException("인증 정보가 없습니다.");
		}

		Object principal = authentication.getPrincipal();
		String authType = authentication.getClass().getSimpleName();
		Long userId = extractUserId(principal);
		AuthorizedRole authorizedRole = getAuthorizedRole(joinPoint);

		List<Role> triedRoles = new ArrayList<>();

		for (RoleProvider provider : roleProviders) {
			if (!provider.supports(authType)) continue;

			Role role = provider.provideRole(userId);
			triedRoles.add(role);

			StoreSessionContextHolder.set(StoreSession.of(userId, role));

			if (Arrays.asList(authorizedRole.value()).contains(role)) {
				log.debug("권한 검증 성공 - 인증타입: {}, 현재 역할: {}, 허용 역할: {}",
					authType, role, Arrays.toString(authorizedRole.value()));
				return;
			} else {
				log.warn("역할 불일치 - 인증타입: {}, 현재 역할: {}, 필요 역할: {}",
					authType, role, Arrays.toString(authorizedRole.value()));
			}
		}

		throw new AccessDeniedException(String.format(
			"접근 권한이 없습니다. 현재 역할: %s, 필요 역할: %s, 인증 유형: %s",
			triedRoles, Arrays.toString(authorizedRole.value()), authType
		));
	}

	private AuthorizedRole getAuthorizedRole(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		AuthorizedRole authorizedRole = signature.getMethod().getAnnotation(AuthorizedRole.class);

		if (authorizedRole == null) {
			authorizedRole = joinPoint.getTarget().getClass().getAnnotation(AuthorizedRole.class);
		}
		if (authorizedRole == null) {
			throw new AccessDeniedException("권한 정보가 없습니다.");
		}
		return authorizedRole;
	}


	private Long extractUserId(Object principal) {
		if (principal instanceof Long l) {
			return l;
		}
		if (principal instanceof String s) {
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				throw new AccessDeniedException("유효하지 않은 사용자 정보 형식입니다.");
			}
		}
		throw new AccessDeniedException("유효하지 않은 사용자 정보입니다.");
	}

	@After("@annotation(com.napzak.global.auth.annotation.AuthorizedRole) || @within(com.napzak.global.auth.annotation.AuthorizedRole)")
	public void clearContext() {
		StoreSessionContextHolder.clear();
	}
}
