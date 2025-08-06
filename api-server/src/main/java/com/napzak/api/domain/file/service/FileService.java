package com.napzak.api.domain.file.service;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	@Value("${cloud.s3.bucket}")
	private String bucket;

	private final S3Presigner s3Presigner;

	public URL generatePresignedUrl(String prefix, String fileName) {
		String filePath = generatePath(prefix, fileName);
		return presignPutRequest(bucket, filePath);
	}

	/**
	 * 여러 상품 이미지 파일에 대한 Presigned URL 생성
	 *
	 * @param imageNames 이미지 파일 이름 리스트
	 * @return Presigned URL 매핑
	 */
	public Map<String, String> generateAllPresignedUrls(List<String> imageNames, String prefix) {
		log.info("{} Presigned URL 일괄 생성 요청 - 파일 수: {}", prefix, imageNames.size());

		return imageNames.parallelStream()
			.collect(Collectors.toMap(
					fileName -> fileName,
					fileName -> {
						String filePath = generatePath(prefix, fileName);
						try {
							URL presignedUrl = presignPutRequest(bucket, filePath);
							log.info("Presigned URL 생성 성공 - 파일명: {}", fileName);
							return presignedUrl.toString();
						} catch (Exception e) {
							log.error("Presigned URL 생성 실패 - 파일명: {}, 오류: {}", fileName, e.getMessage());
							throw new RuntimeException("S3 presigned URL 생성 중 오류 발생: " + e.getMessage());
						}
					}
				)
			);
	}

	/**
	 * Build a presigned URL request for S3.
	 *
	 * @param bucket The S3 bucket name.
	 * @param key The file path in the bucket.
	 * @return The generated presigned URL request.
	 */
	private URL presignPutRequest(String bucket, String key) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.build();

		PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
			.putObjectRequest(putObjectRequest)
			.signatureDuration(Duration.ofHours(2))
			.build();

		PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);
		return presigned.url();
	}

	/**
	 * Generate the path for the file in the S3 bucket.
	 *
	 * @param prefix The folder/prefix where the file is stored.
	 * @param fileName The name of the file.
	 * @return The full file path in the S3 bucket.
	 */
	private String generatePath(String prefix, String fileName) {
		String fileId = UUID.randomUUID().toString();
		return String.format("%s/%s-%s", prefix, fileId, fileName);
	}
}
