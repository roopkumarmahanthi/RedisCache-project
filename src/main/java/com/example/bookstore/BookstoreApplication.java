package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Bookstore Spring Boot application.
 */
@SpringBootApplication
public class BookstoreApplication {

	/**
	 * Main method that bootstraps the Spring application context.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

}
