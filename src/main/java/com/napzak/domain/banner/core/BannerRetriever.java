package com.napzak.domain.banner.core;

import com.napzak.domain.banner.core.entity.BannerEntity;
import com.napzak.domain.banner.core.vo.Banner;
import com.napzak.domain.product.core.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BannerRetriever {

    private final BannerRepository bannerRepository;

    public List<Banner> findAllBanners(){

        List<BannerEntity> bannerEntityList = bannerRepository.findAll();

        return bannerEntityList.stream()
                .map(Banner::fromEntity)
                .collect(Collectors.toList());
    }
}
