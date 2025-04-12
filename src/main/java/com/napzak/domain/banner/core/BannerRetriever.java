package com.napzak.domain.banner.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.banner.core.entity.BannerEntity;
import com.napzak.domain.banner.core.entity.enums.BannerType;
import com.napzak.domain.banner.core.vo.Banner;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BannerRetriever {

	private final BannerRepository bannerRepository;

	public List<Banner> findAllBanners(BannerType bannerType) {

		List<BannerEntity> bannerEntityList = bannerRepository.findAllByBannerType(bannerType);

		return bannerEntityList.stream()
			.map(Banner::fromEntity)
			.toList();
	}
}