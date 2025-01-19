package com.napzak.global.external.s3.api.service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.napzak.global.external.s3.api.dto.ProductPresignedUrlFindAllResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	@Value("${cloud.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	/**
	 * Generate a presigned URL for the given file path and method.
	 *
	 * @param prefix The folder/prefix where the file is stored.
	 * @param fileName The name of the file.
	 * @return The generated presigned URL.
	 */
	public URL generatePresignedUrl(String prefix, String fileName) {
		String filePath = generatePath(prefix, fileName);
		return amazonS3.generatePresignedUrl(buildPresignedUrlRequest(bucket, filePath));
	}

	/**
	 * 여러 상품 이미지 파일에 대한 Presigned URL 생성
	 *
	 * @param productImages 이미지 파일 이름 리스트
	 * @return Presigned URL 매핑
	 */
	public ProductPresignedUrlFindAllResponse generateAllPresignedUrlsForProduct(List<String> productImages){
		log.info("상품 Presigned URL 일괄 생성 요청 - 파일 수: {}", productImages.size());

		Map<String, String> productPresignedUrls = productImages.parallelStream()
			.collect(Collectors.toMap(
				productImage -> productImage,
				productImage -> {
					String filePath = generatePath("product", productImage);
					try {
						URL presignedUrl = amazonS3.generatePresignedUrl(buildPresignedUrlRequest(bucket, filePath));
						log.info("Presigned URL 생성 성공 - 파일명: {}", productImage);
						return presignedUrl.toString();
					} catch (Exception e) {
						log.error("Presigned URL 생성 실패 - 파일명: {}, 오류: {}", productImage, e.getMessage());
						throw new RuntimeException("S3 presigned URL 생성 중 오류 발생: " + e.getMessage());
					}
				}
			));
		log.info("모든 Presigned URL 생성 완료");

		return ProductPresignedUrlFindAllResponse.from(productPresignedUrls);
	}

	/**
	 * Build a presigned URL request for S3.
	 *
	 * @param bucket The S3 bucket name.
	 * @param filePath The file path in the bucket.
	 * @return The generated presigned URL request.
	 */
	private GeneratePresignedUrlRequest buildPresignedUrlRequest(String bucket, String filePath) {
		return new GeneratePresignedUrlRequest(bucket, filePath)
			.withMethod(HttpMethod.PUT)
			.withExpiration(generatePresignedUrlExpiration());
	}

	/**
	 * Generate the expiration date for the presigned URL (default 2 hours).
	 *
	 * @return The expiration date.
	 */
	private Date generatePresignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60 * 2; // 2 hours
		expiration.setTime(expTimeMillis);
		return expiration;
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
