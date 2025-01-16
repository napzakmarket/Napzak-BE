package com.napzak.domain.banner.api.controller;

import com.napzak.domain.banner.api.dto.HomeBannerResponse;
import com.napzak.global.common.exception.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BannerApi {

    ResponseEntity<SuccessResponse<List<HomeBannerResponse>>> getBanners();

}
