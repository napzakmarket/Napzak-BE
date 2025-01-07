package com.napzak.global.external.s3.api.service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

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
	 * @param method The HTTP method for the presigned URL (PUT or GET).
	 * @return The generated presigned URL.
	 */
	public URL generatePresignedUrl(String prefix, String fileName, HttpMethod method) {
		String filePath = generatePath(prefix, fileName);
		return amazonS3.generatePresignedUrl(buildPresignedUrlRequest(bucket, filePath, method));
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

	/**
	 * Build a presigned URL request for S3.
	 *
	 * @param bucket The S3 bucket name.
	 * @param filePath The file path in the bucket.
	 * @param method The HTTP method (PUT or GET).
	 * @return The generated presigned URL request.
	 */
	private GeneratePresignedUrlRequest buildPresignedUrlRequest(String bucket, String filePath, HttpMethod method) {
		return new GeneratePresignedUrlRequest(bucket, filePath)
			.withMethod(method)
			.withExpiration(generatePresignedUrlExpiration());
	}

	/**
	 * Generate the expiration date for the presigned URL (default 2 hours).
	 *
	 * @return The expiration date.
	 */
	private Date generatePresignedUrlExpiration() {
		Date expiration = new Date();
		expiration.setTime(expiration.getTime() + 1000 * 60 * 60 * 2); // 2 hours
		return expiration;
	}
}
