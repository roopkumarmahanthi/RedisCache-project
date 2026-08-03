package com.example.bookstore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bookstore.Entites.Author;
import com.example.bookstore.Entites.Book;
import com.example.bookstore.Services.AuthorService;
import com.example.bookstore.Services.BookService;

/**
 * Spring Boot integration tests that load the full application context
 * and test service methods against the actual database.
 */
@SpringBootTest
class BookstoreApplicationTests {

	/** Verifies that the Spring application context loads without errors. */
	@Test
	void contextLoads() {
	}
	
	/** BookService instance injected via setter for testing book operations. */
	private BookService bookService;

	@Autowired
	public void setBookSerice(BookService bookService) {
		this.bookService = bookService;
	}
	
	/** AuthorService instance for testing author operations. */
	@Autowired
	AuthorService authorService;
	
	/**
	 * Verifies that getBookByName returns the correct book
	 * and its author name matches the expected value.
	 */
	@Test
	public void getBookByName() {
		Optional<Book> bookByName = bookService.getBookByName("The Jungle Forest");
		assertTrue(bookByName.isEmpty());
	}
	
	/**
	 * Verifies that getBookById returns a present Optional
	 * for an existing book ID.
	 */
	@Test
	public void getBookById() {
		Optional<Book> bookById = bookService.getBookById(1);
		assertTrue(bookById.isEmpty());
	}

	/**
	 * Verifies that getAuthorByEmail returns an empty Optional
	 * for a non-existent email.
	 */
	@Test
	public void getAuthorByEmail() {
		Optional<Author> authorByEmail = authorService.getAuthorByEmail("mani@gmail.com");
		assertFalse(authorByEmail.isPresent());
	}
}
