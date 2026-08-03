package com.example.bookstore.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookstore.Entites.Publisher;


/**
 * JPA repository for Publisher entities.
 * Provides custom queries for finding and deleting publishers by name and email,
 * with uniqueness checks that exclude a specific publisher ID.
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
	/** @return publisher wrapped in Optional by name */
	Optional<Publisher> findByPublishername(String publishername);

	/** @return publisher wrapped in Optional by email */
	Optional<Publisher> findByEmail(String email);

	/** Deletes a publisher by their name. */
	void deleteByPublishername(String publishername);

	/** Deletes a publisher by their email. */
	void deleteByEmail(String email);

	/** @return publisher with the given name, excluding the publisher with the given ID */
	@Query(value = "Select p from Publisher p where p.publishername= :name and p.publisherid!= :id")
	Optional<Publisher> findPublisherByPublishernameIdNot(@Param("name") String publishername,@Param("id") int publisherid);

	/** @return publisher with the given email, excluding the publisher with the given ID */
	@Query(value = "Select p from Publisher p where p.email= :email and p.publisherid!= :id")
	Optional<Publisher> findPublisherByEmailIdNot(@Param("email") String email,@Param("id") int publisherid);
}
