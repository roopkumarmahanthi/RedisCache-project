package com.example.bookstore.Entites;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing an Author in the bookstore. An author can have multiple
 * books (one-to-many relationship).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable{

	/** Auto-generated unique identifier for the author. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int authorid;

	/** Unique name of the author. */
	@Column(unique = true)
	@NotBlank(message = "Author name is Required")
	private String authorname;

	/** Short biography of the author. */
	@NotBlank(message = "please mention about his biography")
	private String biography;

	/** Unique email address of the author. Only gmail/yahoo domains allowed. */
	@Column(unique = true)
	@Email(regexp = "(?i)^[A-Za-z0-9._+%-]+@(gmail|yahoo)\\.(com|in)$", message = "Please give valid email. Only gmail.com, gmail.in, yahoo.com, yahoo.in are allowed.")
	private String email;

	/** List of books written by this author. Read-only in JSON responses. */
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("author")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<Book> books;

}
