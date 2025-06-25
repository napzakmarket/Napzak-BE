package com.napzak.global.external.s3.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.external.s3.exception.FileErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageCleaner {

	@Value("${cloud.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	//주어진 prefix에 해당하는 S3 key 전체 목록을 가져오는 메서드
	public List<String> listS3Keys(String prefix) {
		try {
			List<String> allS3Keys = new ArrayList<>();
			ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(prefix);
			ListObjectsV2Result result;

			do {
				result = amazonS3.listObjectsV2(request);
				result.getObjectSummaries().forEach(s -> allS3Keys.add(s.getKey()));
				request.setContinuationToken(result.getNextContinuationToken());
			} while (result.isTruncated());

			return allS3Keys;
		} catch (AmazonS3Exception e) {
			throw new NapzakException(FileErrorCode.S3_ACCESS_DENIED);
		} catch (SdkClientException e) {
			throw new NapzakException(FileErrorCode.S3_SERVICE_UNAVAILABLE);
		} catch (Exception e) {
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
				List<DeleteObjectsRequest.KeyVersion> batch = unusedKeys.subList(i,
						Math.min(i + 1000, unusedKeys.size()))
					.stream().map(DeleteObjectsRequest.KeyVersion::new).toList();

				System.out.println("batch = " + batch);
				amazonS3.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(batch));
			}
		} catch (Exception e) {
			log.info("S3 키 삭제 실패", e);
			throw new NapzakException(FileErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
