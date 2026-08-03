package com.example.bookstore.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.bookstore.Entites.Book;
import com.example.bookstore.Entites.BookDTO;

/**
 * Service interface defining business operations for Book management.
 */
public interface BookService {
	/** @return list of all books */
	public List<Book> getAllBooks();

	/** Creates or updates a book from the given DTO, validating author and publisher existence. */
	public void addBook(BookDTO bookDto);

	/** @return book wrapped in Optional by ID */
	public Optional<Book> getBookById(int bookid);

	/** @return book wrapped in Optional by name */
	public Optional<Book> getBookByName(String bookname);

	/** Deletes a book by its ID. */
	public void deleteBookById(int bookid);

	/** Throws NotFoundException if the Optional is empty. */
	public void checkAvailability(Optional<Book> book);

	/** Deletes a book by its name. */
	public void deleteByBookName(String bookname);

	/** Partially updates a book using the provided field map. */
	public void partialUpdate(HashMap<String,Object> data, Book book);

	/** @return list of books with price greater than or equal to the given value */
	public List<Book> getBookByPriceGreaterThanEqual(int price);

	/** @return list of books written by the given author name */
	public List<Book> getBooksByAuthorName(String authorname);

	/** @return list of books published by the given publisher name */
	public List<Book> getBooksByPublisherName(String publishername);
}
