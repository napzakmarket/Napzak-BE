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
import com.napzak.global.common.exception.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/banners")
public class BannerController {

	private final BannerService bannerService;

	@GetMapping("/home")
	public ResponseEntity<SuccessResponse<BannerResponseList>> getBanners() {

		List<HomeBannerResponse> bannerResponse = bannerService.getAllBanners().stream()
			.map(banner -> HomeBannerResponse.of(
				banner.getId(),
				banner.getPhotoUrl(),
				banner.getRedirectUrl(),
				banner.getSequence()
			))
			.toList();

		BannerResponseList response = BannerResponseList.of(bannerResponse);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(BannerSuccessCode.BANNER_GET_SUCCESS, response));
	}

}