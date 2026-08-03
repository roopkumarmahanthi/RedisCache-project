package com.example.bookstore.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource already exists in the database.
 * Maps to HTTP 409 CONFLICT.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {

	/**
	 * @param message the detail message describing the conflict
	 */
	public AlreadyExistsException(String message) {
		super(message);
	}
}
