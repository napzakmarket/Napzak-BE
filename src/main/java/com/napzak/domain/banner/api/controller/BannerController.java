package com.napzak.domain.banner.api.controller;

import com.napzak.domain.banner.api.dto.HomeBannerResponse;
import com.napzak.domain.banner.api.exception.BannerSuccessCode;
import com.napzak.domain.banner.api.service.BannerService;
import com.napzak.domain.product.api.exception.ProductSuccessCode;
import com.napzak.global.common.exception.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/banner")
public class BannerController implements BannerApi{

    private final BannerService bannerService;

    @GetMapping("/home")
    @Override
    public ResponseEntity<SuccessResponse<List<HomeBannerResponse>>> getBanners(){

        List<HomeBannerResponse> homeBannerResponses = bannerService.getAllBanners();
        return ResponseEntity.ok()
                .body(SuccessResponse.of(BannerSuccessCode.BANNER_GET_SUCCESS, homeBannerResponses));
    }

}
