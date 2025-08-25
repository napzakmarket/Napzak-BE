package com.napzak.api.domain.product.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.napzak.domain.product.repository.ProductPhotoRepository;
import com.napzak.domain.product.repository.ProductReportRepository;
import com.napzak.api.domain.file.service.S3ImageCleaner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductPhotoS3ImageCleaner {

	private final ProductPhotoRepository productPhotoRepository;
	private final ProductReportRepository productReportRepository;
	private final S3ImageCleaner s3ImageCleaner;

	@Value("${cloud.s3.base-url}")
	private String baseUrl;

	@Scheduled(cron = "0 00 04 ? * MON", zone = "Asia/Seoul")
	public void cleanUnusedProductImagesScheduled() {
		cleanUnusedProductImages();
	}

	public void cleanUnusedProductImages() {

		Set<String> validKeys = collectValidProductImageKeys();
		List<String> allS3Keys = s3ImageCleaner.listS3Keys("product/");

		List<String> unusedKeys = allS3Keys.stream()
			.filter(key -> !validKeys.contains(key))
			.toList();

		log.info("----------validKeys = {}", validKeys);
		log.info("----------allS3Keys: {}", allS3Keys);
		log.info("----------Unused keys: {}", unusedKeys);

		s3ImageCleaner.deleteS3Keys(unusedKeys);
	}


	//현재 db에 등록되어 있는 product Images를 추출하는 메서드 (삭제하면 안되는 이미지)
	private Set<String> collectValidProductImageKeys() {
		Set<String> keys = new HashSet<>();

		keys.addAll(productPhotoRepository.findAllPhotoUrls().stream()
			.map(this::toKey)
			.collect(Collectors.toSet()));

		keys.addAll(productReportRepository.findAllReportedProductImages().stream()
			.flatMap(images -> Arrays.stream(images.split("\n")))
			.map(this::toKey)
			.collect(Collectors.toSet()));

		return keys;
	}

	//photoUrl에서 prefix를 제거하는 메서드
	private String toKey(String url) {
		return url.replace(baseUrl + "/", "");
	}
}
