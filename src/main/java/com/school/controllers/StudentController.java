package com.school.controllers;

import java.util.Arrays;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.school.dto.Create;
import com.school.dto.StudentsDTO;
import com.school.dto.Update;
import com.school.entities.Students;
import com.school.services.StudentService;
import com.school.utils.CommonConstants;
import com.school.utils.SchoolLogger;
import com.sipios.springsearch.anotation.SearchSpec;

// http://localhost:8080/school/students

@RestController
@RequestMapping("/students")
@Validated
public class StudentController {

	private static final String LOG_STR = "StudentController.%s";

	@Autowired
	private SchoolLogger logger;

	@Autowired
	private StudentService service;

	@GetMapping
	public ResponseEntity<JsonNode> getAllStudents(@SearchSpec Specification<Students> specs) {

		logger.info(String.format(LOG_STR, "getAllStudents")+", queryData = "+specs);

		return service.getAllStudents(specs);
	}

	@GetMapping("/{id}")
	public ResponseEntity<JsonNode> getStudent(
			@PathVariable(name = "id") @Positive(message = "Student id must be a positive value") Long id) {

		logger.info(String.format(LOG_STR, "getStudents") + " , id = " + id);

		return service.getStudent(id);
	}

	@PostMapping
	public ResponseEntity<JsonNode> addStudent(
			@Validated(value = Create.class) @RequestBody(required = true) StudentsDTO studentData,
			BindingResult bindingResults) {

		logger.info(String.format(LOG_STR, "addStudent") + " , studentData = " + studentData);

		if (bindingResults.hasErrors()) {

			ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

			responseNode.put(CommonConstants.RESPONSE, CommonConstants.ERROR);

			ArrayNode errorsArr = responseNode.putArray("errors");

			for (ObjectError error : bindingResults.getAllErrors()) {

				errorsArr.addObject().put(CommonConstants.ERRORCODE, "INVALID_DATA").put(CommonConstants.MESSAGE,
						error.getDefaultMessage());

			}

			logger.error(String.format(LOG_STR, "addStudent") + " , responseNode = " + responseNode);

			return new ResponseEntity<>(responseNode, HttpStatus.BAD_REQUEST);

		}

		return service.addStudent(studentData);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<JsonNode> editStudent(
			@PathVariable(name = "id") @Positive(message = "Student id must be a Positive value") Long id,
			@Validated(value = Update.class) @RequestBody(required = true) StudentsDTO studentData,
			BindingResult bindingResults) {

		logger.info(String.format(LOG_STR, "editStudent") + " , studentData = " + studentData);

		if (bindingResults.hasErrors()) {

			ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

			responseNode.put(CommonConstants.RESPONSE, CommonConstants.ERROR);

			ArrayNode errorsArr = responseNode.putArray("errors");

			for (ObjectError error : bindingResults.getAllErrors()) {

				errorsArr.addObject().put(CommonConstants.ERRORCODE, "INVALID_DATA").put(CommonConstants.MESSAGE,
						error.getDefaultMessage());

			}

			logger.error(String.format(LOG_STR, "editStudent") + " , responseNode = " + responseNode);

			return new ResponseEntity<>(responseNode, HttpStatus.BAD_REQUEST);

		}

		studentData.setId(id);

		return service.editStudent(studentData);

	}

	@DeleteMapping("/{ids}")
	public ResponseEntity<JsonNode> deleteStudents(@PathVariable(name = "ids") Long... ids) {

		logger.info(String.format(LOG_STR, "getStudents") + " , ids = " + Arrays.toString(ids));

		return service.deleteStudents(ids);
	}
	
	@DeleteMapping
	public ResponseEntity<JsonNode> deleteAllStudents() {

		logger.info(String.format(LOG_STR, "getStudents") );

		return service.deleteAllStudents();
	}
	
}
