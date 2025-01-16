package com.napzak.domain.banner.api.service;

import com.napzak.domain.banner.core.BannerRetriever;
import com.napzak.domain.banner.core.vo.Banner;
import com.napzak.domain.banner.api.dto.HomeBannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRetriever bannerRetriever;

    public List<HomeBannerResponse> getAllBanners(){

        List<Banner> allBanners = bannerRetriever.findAllBanners();

        return allBanners.stream()
                .map(banner -> HomeBannerResponse.of(
                        banner.getId(),
                        banner.getPhotoUrl(),
                        banner.getRedirectUrl(),
                        banner.getSequence()
                ))
                .toList();
    }
}
