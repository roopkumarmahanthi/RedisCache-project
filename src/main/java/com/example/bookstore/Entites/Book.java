package com.example.bookstore.Entites;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Book in the bookstore.
 * A book is associated with one Author and one Publisher.
 * When creating a book, the author and publisher must already exist.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable{

	/** Auto-generated unique identifier using a sequence generator. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//SEQUENCE, generator = "boook_seq")
	//@SequenceGenerator(name = "boook_seq", sequenceName = "book_sequence", initialValue = 100, allocationSize = 50)
	private int bookid;

	/** Unique name of the book. */
	@NotBlank(message = "Book name is Required")
	@Column(unique = true)
	private String bookname;

	/** Date the book was published. */
	@Column(name = "publisheddate")
	private LocalDate date;

	/** Description or summary of the book. */
	@NotBlank(message = "please describe about the Book")
	private String description;

	/** Price of the book. Must be a positive value. */
	@Positive(message = "Price must be positive")
	private int price;

	/** The author who wrote this book. */
	@NotNull(message = "please select the Author")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("books")
	@JoinColumn(name="authorid")
	private Author author;

	/** The publisher who published this book. */
	@NotNull(message = "please select the Publisher")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("books")
	@JoinColumn(name = "publisherid")
	private Publisher publisher;
}
