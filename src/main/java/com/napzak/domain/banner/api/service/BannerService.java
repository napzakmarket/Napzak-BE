package com.napzak.domain.banner.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.napzak.domain.banner.core.BannerRetriever;
import com.napzak.domain.banner.core.vo.Banner;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerService {

	private final BannerRetriever bannerRetriever;

	public List<Banner> getAllBanners() {

		return bannerRetriever.findAllBanners();
	}
}