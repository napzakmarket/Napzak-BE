package com.napzak.api.domain.banner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.napzak.domain.banner.crud.BannerRetriever;
import com.napzak.domain.banner.entity.enums.BannerType;
import com.napzak.domain.banner.vo.Banner;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerService {

	private final BannerRetriever bannerRetriever;

	public List<Banner> getAllBanners(BannerType bannerType) {

		return bannerRetriever.findAllBanners(bannerType);
	}
}