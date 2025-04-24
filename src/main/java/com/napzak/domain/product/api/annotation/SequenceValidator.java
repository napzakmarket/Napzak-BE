package com.napzak.domain.product.api.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import com.napzak.domain.product.api.dto.request.ProductPhotoBase;
import com.napzak.domain.product.api.dto.request.ProductPhotoRequestDto;

public class SequenceValidator implements ConstraintValidator<ValidSequence, List<?>> {

	@Override
	public boolean isValid(List<?> value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return false; // 빈 리스트는 유효하지 않음
		}

		// ProductPhotoBase 타입인지 확인
		if (!(value.get(0) instanceof ProductPhotoBase)) {
			return false;
		}

		List<ProductPhotoBase> productPhotoList = value.stream()
			.filter(ProductPhotoBase.class::isInstance)
			.map(ProductPhotoBase.class::cast)
			.toList();


		List<Integer> sequenceList = productPhotoList.stream()
			.map(ProductPhotoBase::sequence)
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
