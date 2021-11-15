package com.school.utils;

import java.util.Collection;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestBodyValidator implements ConstraintValidator<ValidRequestBody, Object> {

	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid( Object objectNode, ConstraintValidatorContext context ) {
		
		System.out.println("RequestBodyValidator ");

		ObjectMapper oMapper = new ObjectMapper();

		Map<String, Object> columnsDataMap = oMapper.convertValue( objectNode, Map.class );

		Collection<Object> objectValues = columnsDataMap.values();
		

		return !objectValues.stream().allMatch( obj -> obj == null || obj.toString().equals( "{}" ) );

	}
}
