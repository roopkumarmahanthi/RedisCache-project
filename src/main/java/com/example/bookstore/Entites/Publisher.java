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
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Publisher in the bookstore.
 * A publisher can be associated with multiple books (one-to-many relationship).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher implements Serializable{
	/** Auto-generated unique identifier for the publisher. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)     	
	private int publisherid;
	
	/** Name of the publisher. */
	@NotBlank(message = "Publisher name is Required")
	private String publishername;

	/** Phone number of the publisher. Must start with 6-9 and be exactly 10 digits. */
	@NotBlank(message = "please give valid phonenumber that starts with 6-9 and must contain exactly 10 digits")
	@Pattern(regexp = "^[6-9]\\d{9}",message = "please give valid phonenumber that starts with 6-9 "
			+ "and must contain exactly 10 digits")
	private String phonenumber;
	
	/** Unique email address of the publisher. Only gmail/yahoo domains allowed. */
	@Column(unique = true)
	@Email( regexp = "(?i)^[A-Za-z0-9._+%-]+@(gmail|yahoo)\\.(com|in)$"
			,message = "Please give valid email. Only gmail.com, gmail.in, yahoo.com, yahoo.in are allowed.")
	private String email;

	/** List of books published by this publisher. Read-only in JSON responses. */
	@OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL,orphanRemoval = true)
	@JsonIgnoreProperties("publisher")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<Book> books;
	
}
