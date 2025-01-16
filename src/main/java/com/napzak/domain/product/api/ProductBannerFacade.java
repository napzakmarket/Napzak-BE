package com.napzak.domain.product.api;

import com.napzak.domain.banner.core.BannerRetriever;
import com.napzak.domain.banner.core.vo.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductBannerFacade {

    private final BannerRetriever bannerRetriever;

    public List<Banner> findAllBanners(){
        return bannerRetriever.findAllBanners();
    }
}
