package com.napzak.domain.admin.crud;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.admin.code.AdminErrorCode;
import com.napzak.domain.admin.entity.AdminEntity;
import com.napzak.domain.admin.repository.AdminRepository;
import com.napzak.domain.admin.vo.Admin;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminRetriever {

	private final AdminRepository adminRepository;

	@Transactional(readOnly = true)
	public Admin findByLoginId(final String loginId) {
		AdminEntity adminEntity = adminRepository.findByLoginId(loginId)
			.orElseThrow(() -> new NapzakException(AdminErrorCode.ADMIN_NOT_FOUND));
		return Admin.fromEntity(adminEntity);
	}
}
