package com.napzak.global.common.annotation;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
	private Set<String> validValues;

	@Override
	public void initialize(ValidEnum annotation) {
		validValues = Arrays.stream(annotation.enumClass().getEnumConstants())
			.map(Enum::name)
			.collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
		return value != null && validValues.contains(value.name());
	}
}
