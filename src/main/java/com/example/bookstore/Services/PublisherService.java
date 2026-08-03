package com.example.bookstore.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.bookstore.Entites.Publisher;

/**
 * Service interface defining business operations for Publisher management.
 */
public interface PublisherService {
	/** Saves (creates or updates) a publisher, checking for duplicate name/email. */
	public void addPublisher(Publisher publisher);

	/** @return list of all publishers */
	public List<Publisher> getAllPublishers();

	/** @return publisher wrapped in Optional by ID */
	public Optional<Publisher> getPublisherById(int publisherid);

	/** @return publisher wrapped in Optional by name */
	public Optional<Publisher> getPublisherByName(String name);

	/** @return publisher wrapped in Optional by email */
	public Optional<Publisher> getPublisherByEmail(String email);

	/** Deletes a publisher by their ID. */
	public void deleteByPublisherId(int publisherid);

	/** Throws NotFoundException if the Optional is empty. */
	public void checkAvailability(Optional<Publisher> publisher);

	/** Deletes a publisher by their name. */
	public void deleteByPublisherName(String publishername);

	/** Deletes a publisher by their email. */
	public void deleteByPublisherEmail(String email);

	/** Partially updates a publisher using the provided field map. */
	public void partialUpdate(HashMap<String,Object> data, Publisher publisher);
}
