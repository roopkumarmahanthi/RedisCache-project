package com.example.bookstore.ExceptionHandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for the bookstore application.
 * Intercepts specific exceptions and returns structured error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles NotFoundException and returns a 404 NOT FOUND response.
	 *
	 * @param exception the thrown NotFoundException
	 * @param request   the current web request
	 * @return ResponseEntity with error details
	 */
	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> notFound(NotFoundException exception,WebRequest request){
		ErrorResponseDTO error= new ErrorResponseDTO(
				LocalDateTime.now(),
				request.getDescription(false),
				HttpStatus.NOT_FOUND,
				exception.getMessage()
				);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(error);
	}
	
	/**
	 * Handles AlreadyExistsException and returns a 409 CONFLICT response.
	 *
	 * @param exception the thrown AlreadyExistsException
	 * @param request   the current web request
	 * @return ResponseEntity with error details
	 */
	@ExceptionHandler(value = AlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDTO> alreadyEixsts(AlreadyExistsException exception,WebRequest request){
		ErrorResponseDTO error= new ErrorResponseDTO(
				LocalDateTime.now(),
				request.getDescription(false),
				HttpStatus.CONFLICT,
				exception.getMessage()
				);
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(error);
	}
	
	/**
	 * Handles MethodArgumentNotValidException (bean validation failures) and returns a 400 BAD REQUEST response.
	 *
	 * @param exception the thrown MethodArgumentNotValidException
	 * @param request   the current web request
	 * @return ResponseEntity with the first field error message
	 */
	@ExceptionHandler(value=MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> inValidData(MethodArgumentNotValidException exception,WebRequest request){
		ErrorResponseDTO error= new ErrorResponseDTO(
				LocalDateTime.now(),
				request.getDescription(false),
				HttpStatus.BAD_REQUEST,
				exception.getBindingResult().getFieldError().getDefaultMessage()
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	
	/**
	 * Handles ConstraintViolationException (path/query param validation failures) and returns a 400 BAD REQUEST response.
	 *
	 * @param exception the thrown ConstraintViolationException
	 * @param request   the current web request
	 * @return ResponseEntity with the first constraint violation message
	 */
	@ExceptionHandler(value=ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDTO> inValidData(ConstraintViolationException exception,WebRequest request){
		ErrorResponseDTO error= new ErrorResponseDTO(
				LocalDateTime.now(),
				request.getDescription(false),
				HttpStatus.BAD_REQUEST,
				exception.getConstraintViolations().iterator().next().getMessage()
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	
	/**
	 * Handles IllegalArgumentException (invalid field in partial update) and returns a 400 BAD REQUEST response.
	 *
	 * @param exception the thrown IllegalArgumentException
	 * @param request   the current web request
	 * @return ResponseEntity with error details
	 */
	@ExceptionHandler(value=IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDTO> inValidData(IllegalArgumentException exception,WebRequest request){
		ErrorResponseDTO error= new ErrorResponseDTO(
				LocalDateTime.now(),
				request.getDescription(false),
				HttpStatus.BAD_REQUEST,
				exception.getMessage()
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	
}
