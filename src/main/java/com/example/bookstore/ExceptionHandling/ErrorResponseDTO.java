package com.example.bookstore.ExceptionHandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a structured error response returned by the GlobalExceptionHandler.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
	/** Timestamp when the error occurred. */
	private LocalDateTime dateTime;

	/** The request path that triggered the error. */
	private String path;

	/** The HTTP status of the error. */
	private HttpStatus httpStatus;

	/** A human-readable error message. */
	private String errormessage;
}
