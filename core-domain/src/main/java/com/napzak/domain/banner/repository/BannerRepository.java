package com.napzak.domain.banner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.banner.entity.BannerEntity;
import com.napzak.domain.banner.entity.enums.BannerType;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long> {

	List<BannerEntity> findAllByBannerType(BannerType bannerType);

}