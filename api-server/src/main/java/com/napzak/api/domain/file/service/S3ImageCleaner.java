package com.napzak.api.domain.file.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.napzak.api.domain.file.code.FileErrorCode;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageCleaner {

	@Value("${cloud.s3.bucket}")
	private String bucket;

	private final S3Client s3Client;

	//주어진 prefix에 해당하는 S3 key 전체 목록을 가져오는 메서드
	public List<String> listS3Keys(String prefix) {
		try {
			List<String> allS3Keys = new ArrayList<>();
			String continuationToken = null;
			do {
				ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
					.bucket(bucket)
					.prefix(prefix);
				if (continuationToken != null) {
					requestBuilder.continuationToken(continuationToken);
				}
				ListObjectsV2Response result = s3Client.listObjectsV2(requestBuilder.build());
				result.contents().forEach(s -> allS3Keys.add(s.key()));
				continuationToken = result.nextContinuationToken();
			} while (continuationToken != null);
			return allS3Keys;
		} catch (Exception e) {
			log.error("S3 key 조회 실패", e);
			throw new NapzakException(FileErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	//S3 key들 중 unusedKeys만 일괄삭제하는 메서드
	public void deleteS3Keys(List<String> unusedKeys) {
		if (unusedKeys.isEmpty()) {
			return;
		}
		try {
			for (int i = 0; i < unusedKeys.size(); i += 1000) {
				List<ObjectIdentifier> batch = unusedKeys.subList(i, Math.min(i + 1000, unusedKeys.size()))
					.stream().map(k -> ObjectIdentifier.builder().key(k).build()).toList();

				DeleteObjectsRequest request = DeleteObjectsRequest.builder()
					.bucket(bucket)
					.delete(Delete.builder().objects(batch).build())
					.build();

				DeleteObjectsResponse response = s3Client.deleteObjects(request);
				log.info("S3 키 삭제 성공: {}건", response.deleted().size());
			}
		} catch (Exception e) {
			log.error("S3 키 삭제 실패", e);
			throw new NapzakException(FileErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
