package com.school.services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.school.dto.StudentsDTO;
import com.school.entities.Students;
import com.school.repos.StudentsRepository;
import com.school.utils.CommonConstants;
import com.school.utils.SchoolLogger;

@Service
public class StudentService {

	private static final String LOG_STR = "StudentService.%s";

	@Autowired
	private SchoolLogger logger;

	@Autowired
	private StudentsRepository repository;

	public ResponseEntity<JsonNode> getAllStudents() {

		String methodName = "getAllStudents";

		logger.info(String.format(LOG_STR, methodName));

		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

		List<Students> studentsList = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));

		responseNode.put(CommonConstants.RESPONSE, CommonConstants.SUCCESS);

		ArrayNode dataArr = responseNode.putArray("data");

		studentsList.forEach(dataArr::addPOJO);

		logger.info(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}

	public ResponseEntity<JsonNode> getStudent(Long id) {

		String methodName = "getStudent";

		logger.info(String.format(LOG_STR, methodName) + ", id = " + id);

		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

		Optional<Students> student = repository.findById(id);

		if (student.isEmpty()) {

			responseNode.put(CommonConstants.RESPONSE, CommonConstants.ERROR);

			ArrayNode errorsArr = responseNode.putArray(CommonConstants.ERRORS);

			errorsArr.addObject().put(CommonConstants.ERRORCODE, "INVALID_DATA").put(CommonConstants.MESSAGE,
					"Student not available");

			logger.error(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

			return new ResponseEntity<>(responseNode, HttpStatus.NOT_FOUND);

		}
		
		responseNode.put(CommonConstants.RESPONSE, CommonConstants.SUCCESS);

		ArrayNode dataArr = responseNode.putArray("data");

		Arrays.asList(student.get()).forEach(dataArr::addPOJO);

		logger.info(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

		return new ResponseEntity<>(responseNode, HttpStatus.OK);

	}
	
	public ResponseEntity<JsonNode> addStudent(StudentsDTO studentData) {

		String methodName = "addStudent";

		logger.info(String.format(LOG_STR, methodName) + " studentData = " + studentData);

		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

		saveStudent(studentData,null);

		responseNode.put(CommonConstants.RESPONSE, CommonConstants.SUCCESS);

		logger.info(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}

	public ResponseEntity<JsonNode> editStudent(StudentsDTO studentData) {

		String methodName = "editStudent";

		logger.info(String.format(LOG_STR, methodName) + " studentData = " + studentData);

		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

		Optional<Students> student = repository.findById(studentData.getId());

		if (student.isEmpty()) {

			responseNode.put(CommonConstants.RESPONSE, CommonConstants.ERROR);

			ArrayNode errorsArr = responseNode.putArray(CommonConstants.ERRORS);

			errorsArr.addObject().put(CommonConstants.ERRORCODE, "INVALID_DATA").put(CommonConstants.MESSAGE,
					"Student not available");

			logger.error(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

			return new ResponseEntity<>(responseNode, HttpStatus.NOT_FOUND);

		}

		saveStudent(studentData, student.get());

		responseNode.put(CommonConstants.RESPONSE, CommonConstants.SUCCESS);

		logger.info(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}

	private void saveStudent(StudentsDTO studentData, Students student) {

		String methodName = "saveStudent";

		logger.info(String.format(LOG_STR, methodName) + " studentData = " + studentData);
		
		if( Objects.isNull(student)) {
			student =  new Students();
		}

		if (Objects.nonNull(studentData.getName() ) && studentData.getName().isPresent())
			student.setName(studentData.getName().get());

		if ( Objects.nonNull(studentData.getDateOfBirth() ) && studentData.getDateOfBirth().isPresent())
			student.setDateOfBirth(studentData.getDateOfBirth().get());

		if (Objects.nonNull(studentData.getAddress() ) && studentData.getAddress().isPresent())
			student.setAddress(studentData.getAddress().get());

		if (Objects.nonNull(studentData.getGender() ) && studentData.getGender().isPresent())
			student.setGender(studentData.getGender().get());

		if (Objects.nonNull(studentData.getContactNumber() ) && studentData.getContactNumber().isPresent())
			student.setContactNumber(studentData.getContactNumber().get());

		if (Objects.nonNull(studentData.getSports() ) && studentData.getSports().isPresent())
			student.setSports(studentData.getSports().get());

		if (Objects.nonNull(studentData.getCurriculums() ) && studentData.getCurriculums().isPresent())
			student.setCurriculums(studentData.getCurriculums().get());

		repository.save(student);

	}

	public ResponseEntity<JsonNode> deleteStudents(Long[] ids) {

		String methodName = "deleteStudents";

		logger.info(String.format(LOG_STR, methodName) + " ids = " + Arrays.toString(ids));

		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

		Iterable<Long> studentIdItrs = Arrays.asList(ids);

		List<Students> studentList = repository.findAllById(studentIdItrs);

		List<Long> dbStudentIdList = studentList.stream().map(Students::getId).collect(Collectors.toList());

		List<Long> invalidIdList = Arrays.asList(ids).stream().filter(sId -> !dbStudentIdList.contains(sId))
				.collect(Collectors.toList());

		if (!invalidIdList.isEmpty()) {

			responseNode.put(CommonConstants.RESPONSE, CommonConstants.ERROR);

			ArrayNode errorsArr = responseNode.putArray(CommonConstants.ERRORS);
			
			invalidIdList.stream().forEach( sId -> {
				errorsArr.addObject().put(CommonConstants.ERRORCODE, "INVALID_DATA").put(CommonConstants.MESSAGE,
						"Invalid Student Id = "+sId);
			});

			logger.error(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

			return new ResponseEntity<>(responseNode, HttpStatus.NOT_FOUND);

		}
		
		repository.deleteAllInBatch(studentList);
		
		responseNode.put(CommonConstants.RESPONSE, CommonConstants.SUCCESS);

		logger.info(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}
	
	public ResponseEntity<JsonNode> deleteAllStudents() {

		String methodName = "deleteAllStudents";

		logger.info(String.format(LOG_STR, methodName) );

		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

		List<Students> studentList = repository.findAll();

		repository.deleteAllInBatch(studentList);
		
		responseNode.put(CommonConstants.RESPONSE, CommonConstants.SUCCESS);

		logger.info(String.format(LOG_STR, methodName) + " , responseNode = " + responseNode);

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}
	
}