package com.example.bookstore.Repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookstore.Entites.Book;


/**
 * JPA repository for Book entities.
 * Provides custom queries for finding books by name, author, publisher,
 * and uniqueness checks that exclude a specific book ID.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	/** @return book wrapped in Optional by name */
	Optional<Book> findByBookname(String bookname);

	/** Deletes a book by its name. */
	void deleteByBookname(String bookname);

	/** @return list of books written by the given author name */
	@Query("SELECT b FROM Book b JOIN b.author a WHERE a.authorname = :authorName")
	List<Book> findBooksByAuthorName(@Param("authorName") String authorName);

	/** @return list of books published by the given publisher name */
	@Query("SELECT b FROM Book b JOIN b.publisher p WHERE p.publishername = :publisherName")
	List<Book> findBooksByPublisherName(@Param("publisherName") String publisherName);

	/** @return book with the given name, excluding the book with the given ID */
	@Query(value = "Select b from Book b where b.bookname= :name and b.bookid!= :id")
	Optional<Book> findBookByBooknameIdNot(@Param("name") String bookname,@Param("id") int bookid);
}
