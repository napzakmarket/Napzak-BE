package com.napzak.domain.store.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.napzak.domain.store.core.StorePhotoRepository;
import com.napzak.domain.store.core.StoreReportRepository;
import com.napzak.domain.store.core.StoreRepository;
import com.napzak.global.external.s3.api.service.S3ImageCleaner;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorePhotoS3ImageCleaner {

	private final S3ImageCleaner s3ImageCleaner;
	private final StoreRepository storeRepository;
	private final StorePhotoRepository storePhotoRepository;
	private final StoreReportRepository storeReportRepository;

	@Value("${cloud.s3.base-url}")
	private String baseUrl;

	@Scheduled(cron = "0 15 00 ? * WED", zone = "Asia/Seoul")
	public void cleanUnusedStoreImagesScheduled() {
		cleanUnusedStoreImages();
	}

	public void cleanUnusedStoreImages() {

		Set<String> validKeys = collectValidStoreImageKeys();
		List<String> allS3Keys = s3ImageCleaner.listS3Keys("store/");

		List<String> unusedKeys = allS3Keys.stream()
			.filter(key -> !validKeys.contains(key))
			.filter(key -> !key.equals("store/"))
			.toList();

		System.out.println("----------validKeys = " + validKeys);
		System.out.println("----------allS3Keys: " + allS3Keys);
		System.out.println("----------Unused keys: " + unusedKeys);

		s3ImageCleaner.deleteS3Keys(unusedKeys);
	}

	private Set<String> collectValidStoreImageKeys() {
		Set<String> keys = new HashSet<>();

		keyAdder(keys, storeRepository.findAllCover());
		keyAdder(keys, storeRepository.findAllPhoto());
		keyAdder(keys, storePhotoRepository.findAllPhotoUrl());

		keyAdder(keys, storeReportRepository.findAllReportedStoreCover());
		keyAdder(keys, storeReportRepository.findAllReportedStoreProfile());

		return keys;
	}

	private void keyAdder(Set<String> keys, List<String> needToKeys) {
		keys.addAll(needToKeys.stream()
			.map(this::toKey)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet()));
	}

	private String toKey(String url) {
		if (url == null) return null;
		return url.replace(baseUrl + "/", "");
	}
}
