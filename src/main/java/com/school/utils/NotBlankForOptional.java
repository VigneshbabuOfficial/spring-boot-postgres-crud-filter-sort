package com.school.utils;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = { NotBlankForOptionalValidator.class })
public @interface NotBlankForOptional {

	String message() default "must not be blank";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String defaultValue() default "test";
}
