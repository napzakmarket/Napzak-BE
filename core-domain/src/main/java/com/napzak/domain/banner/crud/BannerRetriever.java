package com.napzak.domain.banner.crud;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.banner.entity.BannerEntity;
import com.napzak.domain.banner.entity.enums.BannerType;
import com.napzak.domain.banner.repository.BannerRepository;
import com.napzak.domain.banner.vo.Banner;

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