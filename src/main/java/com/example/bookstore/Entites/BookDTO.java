package com.example.bookstore.Entites;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating or updating a Book.
 * Uses flat author and publisher IDs instead of nested objects.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	/** The book's ID. 0 indicates a new book. */
	private int bookid;

	/** Unique name of the book. */
	@NotBlank(message = "Book name is Required")
	private String bookname;

	/** Date the book was published. */
	private LocalDate date;

	/** Description or summary of the book. */
	@NotBlank(message = "please describe about the Book")
	private String description;

	/** Price of the book. Must be a positive value. */
	@Positive(message = "Price must be positive")
	private int price;

	/** ID of the author who wrote this book. */
	@NotNull(message = "please select the Author")
	private int authorid;

	/** ID of the publisher who published this book. */
	@NotNull(message = "please select the Publisher")
	private int publisherid;
}
