package com.napzak.domain.banner.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.domain.banner.api.dto.response.BannerResponseList;
import com.napzak.domain.banner.api.dto.response.HomeBannerResponse;
import com.napzak.domain.banner.api.exception.BannerSuccessCode;
import com.napzak.domain.banner.api.service.BannerService;
import com.napzak.domain.banner.core.entity.enums.BannerType;
import com.napzak.global.common.exception.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/banners")
public class BannerController implements BannerApi {

	private final BannerService bannerService;

	@Override
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
				banner.getSequence()
			))
			.toList();
	}

}