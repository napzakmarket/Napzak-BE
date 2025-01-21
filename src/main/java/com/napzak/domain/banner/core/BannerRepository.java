package com.napzak.domain.banner.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.banner.core.entity.BannerEntity;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long> {

}