package com.napzak.domain.banner.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.banner.core.entity.BannerEntity;
import com.napzak.domain.banner.core.entity.enums.BannerType;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long> {

	List<BannerEntity> findAllByBannerType(BannerType bannerType);

}