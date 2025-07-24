package com.napzak.api.domain.banner.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.domain.banner.dto.response.BannerResponseList;
import com.napzak.api.domain.banner.dto.response.HomeBannerResponse;
import com.napzak.api.domain.banner.code.BannerSuccessCode;
import com.napzak.api.domain.banner.service.BannerService;
import com.napzak.common.auth.annotation.AuthorizedRole;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.domain.banner.entity.enums.BannerType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/banners")
public class BannerController implements BannerApi {

	private final BannerService bannerService;

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE, Role.REPORTED})
	@GetMapping("/home")
	public ResponseEntity<SuccessResponse<BannerResponseList>> getBanners() {

		List<HomeBannerResponse> topBannerResponse = bannerResponseGenerator(BannerType.TOP);
		List<HomeBannerResponse> middleBannerResponse = bannerResponseGenerator(BannerType.MIDDLE);
		List<HomeBannerResponse> bottomBannerResponse = bannerResponseGenerator(BannerType.BOTTOM);

		BannerResponseList response = BannerResponseList.of(topBannerResponse, middleBannerResponse,
			bottomBannerResponse);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(BannerSuccessCode.BANNER_GET_SUCCESS, response));
	}

	private List<HomeBannerResponse> bannerResponseGenerator(BannerType bannerType) {

		return bannerService.getAllBanners(bannerType).stream()
			.map(banner -> HomeBannerResponse.of(
				banner.getId(),
				banner.getPhotoUrl(),
				banner.getRedirectUrl(),
				banner.getSequence(),
				banner.getIsExternal()
			))
			.toList();
	}

}