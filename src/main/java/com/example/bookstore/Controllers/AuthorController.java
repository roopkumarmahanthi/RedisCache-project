package com.example.bookstore.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.example.bookstore.Entites.Author;
import com.example.bookstore.Services.AuthorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * REST controller for managing Author resources.
 * Provides endpoints for CRUD operations on authors.
 */
@RestController
@RequestMapping("/Author")
@Validated
public class AuthorController {

	private final AuthorService authorService;

	/**
	 * Constructs an AuthorController with the given AuthorService.
	 *
	 * @param authorService the service used to handle author business logic
	 */
	public AuthorController(AuthorService authorService) {
		this.authorService=authorService;
	}
	
	/**
	 * Retrieves all authors.
	 *
	 * @return ResponseEntity containing a list of all authors
	 */
	@GetMapping("/allAuthors")
	public ResponseEntity<Map<String,Object>> getAllAuthors(){
		List<Author> allAuthors = authorService.getAllAuthors();
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Authors",allAuthors));
	}
	
	/**
	 * Retrieves an author by their ID.
	 *
	 * @param authorid the ID of the author to retrieve
	 * @return ResponseEntity containing the matched author
	 */
	@GetMapping("/getAuthorById/{authorid}")
	public ResponseEntity<Map<String,Object>> getAuthor(@PathVariable int authorid){
		Optional<Author> authorById = authorService.getAuthorById(authorid);
		authorService.checkAvailability(authorById);
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Author",authorById));
	}
	
	/**
	 * Retrieves an author by their email address.
	 * Only gmail.com, gmail.in, yahoo.com, yahoo.in are accepted.
	 *
	 * @param email the valid email address of the author
	 * @return ResponseEntity containing the matched author
	 */
	@GetMapping("/getAuthorByEmail/{email}")
	public ResponseEntity<Map<String,Object>> getAuthorByEmail(@PathVariable 
			@Email( regexp = "(?i)^[A-Za-z0-9._+%-]+@(gmail|yahoo)\\.(com|in)$"
			,message = "Please give valid email. Only gmail.com, gmail.in, yahoo.com, yahoo.in are allowed.")
			String email){
		Optional<Author> authorByEmail = authorService.getAuthorByEmail(email);
		authorService.checkAvailability(authorByEmail);
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Author",authorByEmail));
	}
	
	/**
	 * Retrieves an author by their name.
	 *
	 * @param authorname the name of the author (must not be blank)
	 * @return ResponseEntity containing the matched author
	 */
	@GetMapping("/getAuthorByName/{authorname}")
	public ResponseEntity<Map<String,Object>> getAuthorByName(@PathVariable @NotBlank String authorname){
		Optional<Author> authorByName = authorService.getAuthorByName(authorname);
		authorService.checkAvailability(authorByName);
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Author",authorByName));
	}
	
	/**
	 * Adds a new author.
	 *
	 * @param author the author object to be created (validated)
	 * @return ResponseEntity with a success message
	 */
	@PostMapping("/addAuthor")
	public ResponseEntity<Map<String,Object>> addAuthor(@Valid @RequestBody Author author){
		authorService.saveAuthor(author);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message","Author added succussfully"));
	}
	
	/**
	 * Fully updates an existing author.
	 * Note: Providing existing book details may cause a conflict if the book already exists.
	 *
	 * @param author the author object with updated fields (validated)
	 * @return ResponseEntity with a success message
	 */
	@PutMapping("/updateAuthor")
	public ResponseEntity<Map<String,Object>> updateAuthor(@Valid @RequestBody Author author){
		Optional<Author> authorById = authorService.getAuthorById(author.getAuthorid());
		authorService.checkAvailability(authorById);
		authorService.saveAuthor(author);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message","Author updated succussfully"));
	}
	
	/**
	 * Partially updates an existing author's fields.
	 * Books should not be removed to avoid data inconsistency; only updates are allowed.
	 *
	 * @param data     a map of fields to update
	 * @param authorid the ID of the author to partially update
	 * @return ResponseEntity with a success message, or 400 if data is empty
	 */
	@PatchMapping("/partialUpdate/{authorid}")
	public ResponseEntity<Map<String,Object>> partialUpdate(@RequestBody HashMap<String,Object> data, @PathVariable int authorid){
		if(data.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message","please give valid data"));
		}
		Optional<Author> authorById = authorService.getAuthorById(authorid);
		authorService.checkAvailability(authorById);
		authorService.partailUpdate(data, authorById.get());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message","Author updated succussfully"));
	}
	
	/**
	 * Deletes an author by their ID.
	 *
	 * @param authorid the ID of the author to delete
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deleteAuthorById/{authorid}")
	public ResponseEntity<Map<String,Object>> deleteAuthorById(@PathVariable int authorid){
		Optional<Author> authorById = authorService.getAuthorById(authorid);
		authorService.checkAvailability(authorById);
		authorService.deleteByAuthorid(authorid);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","Author deleted succussfully"));
	}
	
	/**
	 * Deletes an author by their name.
	 *
	 * @param authorname the name of the author to delete (must not be blank)
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deleteAuthorByName/{authorname}")
	public ResponseEntity<Map<String,Object>> deleteAuthorByName(@PathVariable @NotBlank String authorname){
		Optional<Author> authorByName = authorService.getAuthorByName(authorname);
		authorService.checkAvailability(authorByName);
		authorService.deleteByAuthorName(authorname);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","Author deleted succussfully"));
	}
	
	/**
	 * Deletes an author by their email address.
	 * Only gmail.com, gmail.in, yahoo.com, yahoo.in are accepted.
	 *
	 * @param email the valid email address of the author to delete
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deleteAuthorByEmail/{email}")
	public ResponseEntity<Map<String,Object>> deleteAuthorByEmail(@PathVariable 
			@Email( regexp = "(?i)^[A-Za-z0-9._+%-]+@(gmail|yahoo)\\.(com|in)$"
			,message = "Please give valid email. Only gmail.com, gmail.in, yahoo.com, yahoo.in are allowed.")
			String email){
		Optional<Author> authorByEmail = authorService.getAuthorByName(email);
		authorService.checkAvailability(authorByEmail);
		authorService.deleteByEmail(email);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","Author deleted succussfully"));
	}
}
