package com.napzak.domain.banner.core;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.napzak.domain.banner.core.entity.BannerEntity;
import com.napzak.domain.banner.core.vo.Banner;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BannerRetriever {

	private final BannerRepository bannerRepository;

	public List<Banner> findAllBanners() {

		List<BannerEntity> bannerEntityList = bannerRepository.findAll();

		return bannerEntityList.stream()
			.map(Banner::fromEntity)
			.collect(Collectors.toList());
	}
}