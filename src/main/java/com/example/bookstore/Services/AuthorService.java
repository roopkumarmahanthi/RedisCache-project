package com.example.bookstore.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.bookstore.Entites.Author;

/**
 * Service interface defining business operations for Author management.
 */
public interface AuthorService {

	/** @return list of all authors */
	public List<Author> getAllAuthors();

	/** Saves (creates or updates) an author, checking for duplicate name/email. */
	public void saveAuthor(Author author);

	/** @return author wrapped in Optional by ID */
	public Optional<Author> getAuthorById(int authorid);

	/** @return author wrapped in Optional by email */
	public Optional<Author> getAuthorByEmail(String email);

	/** Throws NotFoundException if the Optional is empty. */
	public void checkAvailability(Optional<Author> author);

	/** @return author wrapped in Optional by name */
	public Optional<Author> getAuthorByName(String name);

	/** Deletes an author by their ID. */
	public void deleteByAuthorid(int authorid);

	/** Partially updates an author using the provided field map. */
	public void partailUpdate(HashMap<String,Object> data, Author author);

	/** Deletes an author by their name. */
	public void deleteByAuthorName(String authorname);

	/** Deletes an author by their email. */
	public void deleteByEmail(String email);
}
