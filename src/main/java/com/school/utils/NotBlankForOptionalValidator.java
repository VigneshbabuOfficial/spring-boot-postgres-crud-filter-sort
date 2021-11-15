package com.school.utils;

import java.util.Objects;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankForOptionalValidator implements ConstraintValidator<NotBlankForOptional, Optional<?>> {


	@Override
	public boolean isValid( Optional<?> value, ConstraintValidatorContext context ) {
		
		System.out.println("NotBlankForOptionalValidator");

		if ( Objects.isNull( value ) || value.isEmpty() || ( value.isPresent() &&  String.valueOf(value.get()).isBlank()  ) ) {
			return false;
		} else {

			return true;
		}
	}
}
