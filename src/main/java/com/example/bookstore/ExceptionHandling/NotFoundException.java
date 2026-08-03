package com.example.bookstore.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found in the database.
 * Maps to HTTP 404 NOT FOUND.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	/**
	 * @param message the detail message describing what was not found
	 */
	public NotFoundException(String message) {
		super(message);
	}
}
