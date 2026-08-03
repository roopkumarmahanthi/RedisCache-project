package com.example.bookstore.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.Entites.Book;
import com.example.bookstore.Entites.BookDTO;
import com.example.bookstore.Services.BookService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * REST controller for managing Book resources.
 * Provides endpoints for CRUD operations on books.
 */
@RestController
@RequestMapping("/Book")
@Validated
public class BookController {
	
	private final BookService bookService;
	
	/**
	 * Constructs a BookController with the given BookService.
	 *
	 * @param bookService the service used to handle book business logic
	 */
	public BookController(BookService bookService) {
		this.bookService=bookService;
	}
	
	/**
	 * Retrieves all books along with the total price of all books.
	 *
	 * @return ResponseEntity containing the list of books and total amount
	 */
	@GetMapping("/allBooks")
	public ResponseEntity<Map<String,Object>> getAllBooks(){
		List<Book> allBooks = bookService.getAllBooks();
		OptionalInt allAmount = allBooks.stream().mapToInt(b->b.getPrice()).reduce((x,y)-> x+y);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Books",allBooks,"totalAmountforBooks",allAmount));
	}
	
	/**
	 * Retrieves a book by its ID.
	 *
	 * @param bookid the ID of the book to retrieve
	 * @return ResponseEntity containing the matched book
	 */
	@GetMapping("/getBookById/{bookid}")
	public ResponseEntity<Map<String,Object>> getBook(@PathVariable int bookid){
		Optional<Book> bookById = bookService.getBookById(bookid);
		bookService.checkAvailability(bookById);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Book",bookById));
	}
	
	/**
	 * Retrieves a book by its name.
	 *
	 * @param bookname the name of the book (must not be blank)
	 * @return ResponseEntity containing the matched book
	 */
	@GetMapping("/getBookByName/{bookname}")
	public ResponseEntity<Map<String,Object>> getBook(@PathVariable @NotBlank(message = "Book name is Required") String bookname){
		Optional<Book> bookByName = bookService.getBookByName(bookname);
		bookService.checkAvailability(bookByName);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Book",bookByName));
	}
	
	/**
	 * Retrieves all books with a price greater than or equal to the given value.
	 *
	 * @param price the minimum price threshold
	 * @return ResponseEntity containing matching books, or 404 if none found
	 */
	@GetMapping("/getBookByPrice/{price}")
	public ResponseEntity<Map<String,Object>> getBookByPrice(@PathVariable int price){
		List<Book> booksByPrice = bookService.getBookByPriceGreaterThanEqual(price);
		if(booksByPrice.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("Books","Their is no Book with by Price GreaterThanEqual to it."));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Books",booksByPrice));
	}
	
	/**
	 * Retrieves all books written by the given author name.
	 *
	 * @param authorname the name of the author (must not be blank)
	 * @return ResponseEntity containing matching books, or 404 if none found
	 */
	@GetMapping("/getBookByAuthorName/{authorname}")
	public ResponseEntity<Map<String,Object>> getBookByAuthorName(@PathVariable @NotBlank(message = "Author name is Required") String authorname){
		List<Book> booksByAuthorName = bookService.getBooksByAuthorName(authorname);
		if(booksByAuthorName.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("Books","Their is no Book with Author Name."));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Book",booksByAuthorName));
	}
	
	/**
	 * Retrieves all books published by the given publisher name.
	 *
	 * @param publishername the name of the publisher (must not be blank)
	 * @return ResponseEntity containing matching books, or 404 if none found
	 */
	@GetMapping("/getBookByPublisherName/{publishername}")
	public ResponseEntity<Map<String,Object>> getBookByPublisherName(@PathVariable @NotBlank(message = "Publisher name is Required") String publishername){
		List<Book> booksByPublisherName = bookService.getBooksByPublisherName(publishername);
		if(booksByPublisherName.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("Books","Their is no Book with Publisher Name."));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Book",booksByPublisherName));
	}
	
	/**
	 * Adds a new book.
	 *
	 * @param bookDto the book data transfer object (validated)
	 * @return ResponseEntity with a success message
	 */
	@PostMapping("/addBook")
	public ResponseEntity<Map<String,Object>> addBook(@Valid @RequestBody BookDTO bookDto){
		bookService.addBook(bookDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("Message","book added succussfully"));
	}
	
	/**
	 * Fully updates an existing book.
	 *
	 * @param bookDto the book data transfer object with updated fields (validated)
	 * @return ResponseEntity with a success message
	 */
	@PutMapping("/updateBook")
	public ResponseEntity<Map<String,Object>> updateBook(@Valid @RequestBody BookDTO bookDto){
		Optional<Book> bookById = bookService.getBookById(bookDto.getBookid());
		bookService.checkAvailability(bookById);
		bookService.addBook(bookDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("Message","book updated succussfully"));
	}
	
	/**
	 * Partially updates an existing book's fields.
	 *
	 * @param data   a map of fields to update
	 * @param bookid the ID of the book to partially update
	 * @return ResponseEntity with a success message, or 400 if data is empty
	 */
	@PatchMapping("/partialUpdate/{bookid}")
	public ResponseEntity<Map<String,Object>> updateBook(@RequestBody HashMap<String,Object> data, @PathVariable int bookid){
		if(data.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message","please give valid data"));
		}
		Optional<Book> bookById = bookService.getBookById(bookid);
		bookService.checkAvailability(bookById);
		bookService.partialUpdate(data, bookById.get());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("Message","book updated succussfully"));
	}
	
	/**
	 * Deletes a book by its ID.
	 *
	 * @param bookid the ID of the book to delete
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deleteBookById/{bookid}")
	public ResponseEntity<Map<String,Object>> deleteBook(@PathVariable int bookid){
		Optional<Book>bookById = bookService.getBookById(bookid);
		bookService.checkAvailability(bookById);
		bookService.deleteBookById(bookid);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","book deleted succussfully"));
	}
	
	/**
	 * Deletes a book by its name.
	 *
	 * @param bookname the name of the book to delete (must not be blank)
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deleteBookByName/{bookname}")
	public ResponseEntity<Map<String,Object>> deleteBookBYName(@PathVariable @NotBlank String bookname){
		Optional<Book>bookByName = bookService.getBookByName(bookname);
		bookService.checkAvailability(bookByName);
		bookService.deleteByBookName(bookname);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","book deleted succussfully"));
	}
	
}
