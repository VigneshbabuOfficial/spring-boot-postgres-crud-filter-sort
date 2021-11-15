package com.school.exceptions;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.school.utils.CommonConstants;
import com.school.utils.SchoolLogger;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private SchoolLogger logger;

	public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

	/*
	 * @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	 * 
	 * @ExceptionHandler({ InternalServerException.class,
	 * RequestRejectedException.class, Exception.class, ClassCastException.class,
	 * SQLException.class }) public ResponseEntity<JsonNode>
	 * handleInternalServerException(Exception ex, WebRequest request) {
	 * 
	 * logger.error("GlobalExceptionHandler.handleInternalServerException() ", ex);
	 * 
	 * ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	 * 
	 * if (ex.getCause() instanceof NumberFormatException) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * "Invalid Data = " + ex.getMessage().split(":")[2].replaceAll("\"\"",
	 * "").trim(), requestId);
	 * 
	 * } else if (ex.getCause() instanceof UnrecognizedPropertyException) {
	 * 
	 * String errorMsg = ex.getMessage();
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * errorMsg.substring(errorMsg.indexOf("Unrecognized"),
	 * errorMsg.indexOf("(class")), requestId);
	 * 
	 * } else if (ex.getClass() == HttpMessageNotReadableException.class &&
	 * ex.getMessage().contains("JSON parse error:") || ex.getClass() ==
	 * ConstraintDefinitionException.class) {
	 * 
	 * String fieldName = ex.getMessage().split("\n")[1].split("\"")[1];
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * "Input parameter type mismatched for " + fieldName, requestId);
	 * 
	 * } else if (ex.getClass() == HttpMessageNotReadableException.class &&
	 * ex.getMessage().contains("Required request body is missing:")) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * "Required request body is missing", requestId);
	 * 
	 * } else if (ex.getClass() == ConstraintViolationException.class) {
	 * 
	 * List<String> obList = Arrays.asList(ex.getMessage().split(","));
	 * 
	 * ArrayNode errorArr = JsonNodeFactory.instance.arrayNode();
	 * 
	 * for (int i = 0; i < obList.size(); i++) {
	 * 
	 * ClJsonNodeUtil.buildErrorNode(errorArr, ClConstants.INVALID_DATA,
	 * obList.get(i).split(":")[1].trim()); }
	 * 
	 * if (errorArr.size() > 0) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, errorArr, requestId);
	 * 
	 * }
	 * 
	 * } else if (ex.getMessage().contains("anonymousUser")) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_TOKEN,
	 * "No token found", requestId);
	 * 
	 * } else {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.SYSTEM_ERROR,
	 * ClConstants.SYSTEM_ERROR_OCCURRED, requestId);
	 * 
	 * }
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleInternalServerException() responseData = "
	 * + responseData, ex);
	 * 
	 * return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	 * 
	 * }
	 * 
	 * @ResponseStatus(HttpStatus.BAD_REQUEST)
	 * 
	 * @ExceptionHandler({ InvalidRequestParametersException.class,
	 * IllegalArgumentException.class, DateTimeException.class,
	 * NumberFormatException.class, MethodArgumentTypeMismatchException.class })
	 * public ResponseEntity<JsonNode> handleBadRequestException(Exception ex,
	 * WebRequest request) {
	 * 
	 * logger.error("GlobalExceptionHandler.handleBadRequestException()", ex);
	 * 
	 * ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	 * 
	 * if (ex.getCause() instanceof NumberFormatException || ex instanceof
	 * MethodArgumentTypeMismatchException) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * "Invalid Data = " + ex.getMessage().split(":")[2].replaceAll("\"\"", ""),
	 * requestId);
	 * 
	 * } else if (ex instanceof IllegalArgumentException) {
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * ArrayNode errorArr = JsonNodeFactory.instance.arrayNode();
	 * 
	 * try {
	 * 
	 * errorArr.addAll(mapper.readValue(ex.getMessage(), ArrayNode.class));
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, errorArr, requestId);
	 * 
	 * } catch (Exception e) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * ex.getMessage(), requestId);
	 * 
	 * logger.error("GlobalExceptionHandler.handleBadRequestException()", e); }
	 * 
	 * } else {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * ex.getMessage(), requestId); }
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleBadRequestException(), responseData = " +
	 * responseData);
	 * 
	 * return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ResponseStatus(HttpStatus.NOT_FOUND)
	 * 
	 * @ExceptionHandler(NotFoundException.class) public ResponseEntity<JsonNode>
	 * handleNotFoundException(NotFoundException ex, WebRequest request) {
	 * 
	 * logger.error("GlobalExceptionHandler.handleNotFoundException()  ", ex);
	 * 
	 * ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.RESOURCE_NOT_FOUND,
	 * ex.getMessage(), requestId);
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleNotFoundException(), responseData = " +
	 * responseData);
	 * 
	 * return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND); }
	 * 
	 * @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
	 * 
	 * @ExceptionHandler(InsufficientPrivilegesException.class) public
	 * ResponseEntity<JsonNode>
	 * handleInsufficientPrivilegesException(InsufficientPrivilegesException ex,
	 * WebRequest request) {
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleInsufficientPrivilegesException()  ",
	 * ex);
	 * 
	 * ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.ACCESS_DENIED,
	 * ex.getMessage(), requestId);
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleInsufficientPrivilegesException(), responseData = "
	 * + responseData);
	 * 
	 * return new ResponseEntity<>(responseData,
	 * HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); }
	 * 
	 * @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	 * 
	 * @ExceptionHandler(TypeMismatchException.class) public
	 * ResponseEntity<JsonNode> handleMethodNotAllowedException(Exception ex,
	 * WebRequest request) {
	 * 
	 * logger.error("GlobalExceptionHandler.handleMethodNotAllowedException()  ",
	 * ex);
	 * 
	 * ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	 * 
	 * if (ex.getCause() instanceof NumberFormatException) {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.INVALID_DATA,
	 * "Invalid Data = " + ex.getMessage().split(":")[2].replaceAll("\"\"", ""),
	 * requestId);
	 * 
	 * } else {
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.SYSTEM_ERROR,
	 * StringUtils.isBlank(ex.getMessage()) ? INTERNAL_SERVER_ERROR :
	 * ex.getMessage(), requestId);
	 * 
	 * }
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleMethodNotAllowedException(), responseData = "
	 * + responseData);
	 * 
	 * return new ResponseEntity<>(responseData, HttpStatus.METHOD_NOT_ALLOWED); }
	 * 
	 * @ResponseStatus(HttpStatus.UNAUTHORIZED)
	 * 
	 * @ExceptionHandler(InvalidLicenseException.class) public
	 * ResponseEntity<JsonNode> handleInvalidLicenseException(Exception ex,
	 * WebRequest request) {
	 * 
	 * logger.error("GlobalExceptionHandler.handleInvalidLicenseException()  ", ex);
	 * 
	 * ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	 * 
	 * ClJsonNodeUtil.buildNodeError(responseData, ClConstants.ACCESS_DENIED,
	 * "Invalid License", requestId);
	 * 
	 * logger.
	 * error("GlobalExceptionHandler.handleInvalidLicenseException(), responseData = "
	 * + responseData);
	 * 
	 * return new ResponseEntity<>(responseData, HttpStatus.UNAUTHORIZED);
	 * 
	 * }
	 */
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<JsonNode> handleException(Exception ex, WebRequest request) {

		logger.error("GlobalExceptionHandler.handleException() ", ex);

		ObjectNode responseData = JsonNodeFactory.instance.objectNode();
		
		responseData.put(CommonConstants.RESPONSE, CommonConstants.ERROR);

		ArrayNode errorsArr = responseData.putArray(CommonConstants.ERRORS);

		if (ex.getCause() instanceof NumberFormatException) {
			
			errorsArr.addObject().put(CommonConstants.ERRORCODE, CommonConstants.INVALID_DATA).put(CommonConstants.MESSAGE,
					"Invalid Data = " + ex.getMessage().split(":")[2].replaceAll("\"\"", "").trim());

		}  else if (ex.getClass() == HttpMessageNotReadableException.class
				&& ex.getMessage().contains("JSON parse error:")
				|| ex.getClass() == ConstraintDefinitionException.class) {

			String fieldName = ex.getMessage().split("\n")[1].split("\"")[1];
			
			errorsArr.addObject().put(CommonConstants.ERRORCODE, CommonConstants.INVALID_DATA).put(CommonConstants.MESSAGE,
					"Input parameter type mismatched for " + fieldName);

		} else if (ex.getClass() == HttpMessageNotReadableException.class
				&& ex.getMessage().contains("Required request body is missing:")) {

			errorsArr.addObject().put(CommonConstants.ERRORCODE, CommonConstants.INVALID_DATA).put(CommonConstants.MESSAGE,
					"Required request body is missing");

		} else if (ex.getClass() == ConstraintViolationException.class) {

			List<String> obList = Arrays.asList(ex.getMessage().split(","));

			for (int i = 0; i < obList.size(); i++) {
				
				errorsArr.addObject().put(CommonConstants.ERRORCODE, CommonConstants.INVALID_DATA).put(CommonConstants.MESSAGE,
						obList.get(i).split(":")[1].trim());

			}

		} else if( ex.getClass() == HttpRequestMethodNotSupportedException.class  ) {
			
			errorsArr.addObject().put(CommonConstants.ERRORCODE, CommonConstants.INVALID_DATA).put(CommonConstants.MESSAGE,
					ex.getMessage());
			
		} else {
			
			errorsArr.addObject().put(CommonConstants.ERRORCODE, CommonConstants.INVALID_DATA).put(CommonConstants.MESSAGE,
					CommonConstants.SYSTEM_ERROR_OCCURRED);

		}

		logger.error("GlobalExceptionHandler.handleException() responseData = " + responseData, ex);

		return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);

	}
}
