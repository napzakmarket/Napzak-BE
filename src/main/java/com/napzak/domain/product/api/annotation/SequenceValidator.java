package com.napzak.domain.product.api.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import com.napzak.domain.product.api.dto.request.ProductPhotoRequestDto;

public class SequenceValidator implements ConstraintValidator<ValidSequence, List<ProductPhotoRequestDto>> {

	@Override
	public boolean isValid(List<ProductPhotoRequestDto> productPhotoList, ConstraintValidatorContext context) {
		if (productPhotoList == null || productPhotoList.isEmpty()) {
			return false; // 빈 리스트는 유효하지 않음
		}

		List<Integer> sequenceList = productPhotoList.stream()
			.map(ProductPhotoRequestDto::sequence)
			.toList();

		// 중복 체크
		if (new HashSet<>(sequenceList).size() != sequenceList.size()) {
			return false;
		}

		// 1부터 리스트 크기까지 연속성 체크
		int maxSequence = productPhotoList.size();
		List<Integer> expectedSequence = IntStream.rangeClosed(1, maxSequence)
			.boxed()
			.toList();

		return new HashSet<>(sequenceList).containsAll(expectedSequence);
	}
}
