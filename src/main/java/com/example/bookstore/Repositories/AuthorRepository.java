package com.example.bookstore.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookstore.Entites.Author;


/**
 * JPA repository for Author entities.
 * Provides custom queries for finding and deleting authors by name and email,
 * with uniqueness checks that exclude a specific author ID.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
	/** @return author wrapped in Optional by email */
	Optional<Author> findByEmail(String email);

	/** @return author wrapped in Optional by name */
	Optional<Author> findByAuthorname(String authorname);

	/** Deletes an author by their name. */
	void deleteByAuthorname(String authorname);

	/** Deletes an author by their email. */
	void deleteByEmail(String email);

	/** @return author with the given name, excluding the author with the given ID */
	@Query(value = "Select a from Author a where a.authorname= :name and a.authorid!= :id")
	Optional<Author> findAuthorByAuthornameIdNot(@Param("name") String authorname,@Param("id") int authorid);

	/** @return author with the given email, excluding the author with the given ID */
	@Query(value = "Select a from Author a where a.\temail= :email and a.authorid!= :id")
	Optional<Author> findAuthorByEmailIdNot(@Param("email") String email,@Param("id") int authorid);
}
