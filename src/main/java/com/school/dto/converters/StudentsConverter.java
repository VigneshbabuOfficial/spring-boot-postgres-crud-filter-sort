package com.school.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.school.dto.StudentsDTO;
import com.school.entities.Students;
import com.school.utils.SchoolLogger;

public class StudentsConverter implements Converter<Students, StudentsDTO> {

	private static final String LOG_STR = "StudentsConverter.%s";

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SchoolLogger logger;

	@Override
	public StudentsDTO convert(Students entityObj) {

		logger.info(String.format(LOG_STR, "convert"));

		StudentsDTO dtoObj = new StudentsDTO();

		modelMapper.map(entityObj, dtoObj);

		return dtoObj;
	}
}
