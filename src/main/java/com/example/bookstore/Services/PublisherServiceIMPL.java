package com.example.bookstore.Services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookstore.Entites.Publisher;
import com.example.bookstore.Enums.HandleException;
import com.example.bookstore.ExceptionHandling.AlreadyExistsException;
import com.example.bookstore.ExceptionHandling.NotFoundException;
import com.example.bookstore.Repositories.PublisherRepository;

/**
 * Implementation of PublisherService.
 * Handles publisher persistence, duplicate checks, and partial updates via reflection.
 */
@Service
public class PublisherServiceIMPL implements PublisherService {
	
	@Autowired
	private PublisherRepository publisherRepository;

	/**
	 * Saves a publisher. For new publishers (id=0), checks for duplicate name and email.
	 * For existing publishers, checks uniqueness excluding the current publisher's ID.
	 *
	 * @param publisher the publisher to save
	 * @throws AlreadyExistsException if name or email already exists
	 */
	@Override
	public void addPublisher(Publisher publisher) {
		Optional<Publisher> publisherByName = null;
		Optional<Publisher> publisherByEmail = null;
		if(publisher.getPublisherid()==0) {
			publisherByName = publisherRepository.findByPublishername(publisher.getPublishername());
			if(publisherByName.isPresent()) throw new AlreadyExistsException(HandleException.PublisherAlreadyExistsException.getMessage());
		
			publisherByEmail = publisherRepository.findByEmail(publisher.getEmail());
			if(publisherByEmail.isPresent()) throw new AlreadyExistsException(HandleException.PublisherAlreadyExistsException.getMessage());
		}else {
			publisherByName = publisherRepository.findPublisherByPublishernameIdNot(publisher.getPublishername(),publisher.getPublisherid());
			if(publisherByName.isPresent()) throw new AlreadyExistsException(HandleException.PublisherAlreadyExistsException.getMessage());
			
			publisherByEmail = publisherRepository.findPublisherByEmailIdNot(publisher.getEmail(),publisher.getPublisherid());
			if(publisherByEmail.isPresent()) throw new AlreadyExistsException(HandleException.PublisherAlreadyExistsException.getMessage());
		}
		publisherRepository.save(publisher);
	}
	
	/** {@inheritDoc} */
	@Override
	public  List<Publisher> getAllPublishers() {
		return publisherRepository.findAll();
	}
	/** {@inheritDoc} */
	@Override
	public Optional<Publisher> getPublisherById(int publisherid) {
		return publisherRepository.findById(publisherid);
	}
	/** {@inheritDoc} */
	@Override
	public Optional<Publisher> getPublisherByName(String name) {
		return publisherRepository.findByPublishername(name);
	}
	/** {@inheritDoc} */
	@Override
	public Optional<Publisher> getPublisherByEmail(String email) {
		return publisherRepository.findByEmail(email);
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByPublisherId(int publisherid) {
		publisherRepository.deleteById(publisherid);
	}
	/** {@inheritDoc} */
	@Override
	public void checkAvailability(Optional<Publisher> publisher) {
		if(publisher.isEmpty()) throw new NotFoundException(HandleException.PublisherNotFoundException.getMessage());
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByPublisherName(String publishername) {
		publisherRepository.deleteByPublishername(publishername);
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByPublisherEmail(String email) {
		publisherRepository.deleteByEmail(email);
	}
	/**
	 * Partially updates a publisher using reflection.
	 * Updating 'books' is not allowed. Duplicate name/email checks are enforced.
	 *
	 * @param data      map of field names to new values
	 * @param publisher the publisher entity to update
	 * @throws AlreadyExistsException  if name or email conflicts with another publisher
	 * @throws IllegalArgumentException if an invalid field is provided
	 */
	@Override
	public void partialUpdate(HashMap<String,Object> data, Publisher publisher) {
		data.entrySet().stream().forEach((e)->{
			switch(e.getKey()) {
			case "books" -> {
				throw new IllegalArgumentException(HandleException.IllegalData.getMessage());
			}
			case "publishername" ->{
				Optional<Publisher> publisherByName = publisherRepository.findPublisherByPublishernameIdNot((String)e.getValue(), publisher.getPublisherid());
				if(publisherByName.isPresent()) throw new AlreadyExistsException(HandleException.PublisherAlreadyExistsException.getMessage());
			}
			case "email" ->{
				Optional<Publisher> publisherByEmail = publisherRepository.findPublisherByEmailIdNot((String)e.getValue(), publisher.getPublisherid());
				if(publisherByEmail.isPresent()) throw new AlreadyExistsException(HandleException.PublisherAlreadyExistsException.getMessage());
			}
			}
			try {
				Field field= Publisher.class.getDeclaredField(e.getKey());
				field.setAccessible(true);
				field.set(publisher, e.getValue());
			}catch(Exception ex) {
				throw new IllegalArgumentException(HandleException.IllegalData.getMessage());
 			}
		});
		publisherRepository.save(publisher);
	}

}
