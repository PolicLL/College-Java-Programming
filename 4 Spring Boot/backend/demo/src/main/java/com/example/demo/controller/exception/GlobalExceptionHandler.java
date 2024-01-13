package com.example.demo.controller.exception;

import com.example.demo.exception.*;
import com.example.demo.exception.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex, WebRequest request) {

		logger.error("Internal Server Error", ex);
		ErrorResponse errorResponse = new ErrorResponse("Internal Server Error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	@ExceptionHandler(AddressNotFoundException.class)
	public ResponseEntity<?> handleAddressNotFoundException(AddressNotFoundException ex, WebRequest request) {

		logger.warn("Address not found", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<?> handleInvalidInputException(InvalidInputException ex, WebRequest request) {

		logger.warn("Invalid input", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ClientAlreadyExistsWithAddressException.class)
	public ResponseEntity<?> handleClientAlreadyExistsWithAddressException(ClientAlreadyExistsWithAddressException ex,
	                                                                       WebRequest request) {

		logger.warn("Client already exists with address", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<?> handleClientNotFoundException(ClientNotFoundException ex, WebRequest request) {

		logger.warn("Client not found", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<?> handleDeviceNotFoundException(DeviceNotFoundException ex, WebRequest request) {

		logger.warn("Device not found", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(MeasurementConsumptionNotFoundException.class)
	public ResponseEntity<?> handleMeasurementConsumptionNotFoundException(
			MeasurementConsumptionNotFoundException ex, WebRequest request) {

		logger.warn("Measurement consumption not found", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(MeasurementForThisMonthInYearExistsException.class)
	public ResponseEntity<?> handleMeasurementForThisMonthAlreadyExistsException(
			MeasurementForThisMonthInYearExistsException ex, WebRequest request) {

		logger.warn("Measurement for this month already exists", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {

		logger.warn("Unauthorized access", ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}

	// Add more exception handlers for different types of exceptions
}
