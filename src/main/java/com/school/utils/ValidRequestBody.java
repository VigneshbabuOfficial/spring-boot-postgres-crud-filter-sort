package com.school.utils;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = RequestBodyValidator.class)
public @interface ValidRequestBody {
	
	String message() default "Required parameter is missing";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
