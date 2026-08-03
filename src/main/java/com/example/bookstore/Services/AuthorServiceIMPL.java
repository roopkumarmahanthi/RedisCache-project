package com.example.bookstore.Services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.example.bookstore.Entites.Author;
import com.example.bookstore.Enums.HandleException;
import com.example.bookstore.ExceptionHandling.AlreadyExistsException;
import com.example.bookstore.ExceptionHandling.NotFoundException;
import com.example.bookstore.Repositories.AuthorRepository;

/**
 * Implementation of AuthorService.
 * Handles author persistence, duplicate checks, and partial updates via reflection.
 */
@Service
public class AuthorServiceIMPL implements AuthorService{
	@Autowired
	private AuthorRepository authorRepository;

	/** {@inheritDoc} */
	@Override
	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}
	/**
	 * Saves an author. For new authors (id=0), checks for duplicate name and email.
	 * For existing authors, checks uniqueness excluding the current author's ID.
	 *
	 * @param author the author to save
	 * @throws AlreadyExistsException if name or email already exists
	 */
	@Override
	public void saveAuthor(Author author) {
		Optional<Author> authorByEmail=null;
		Optional<Author> authorByName =null;
		if(author.getAuthorid()==0) {
			authorByEmail = getAuthorByEmail(author.getEmail());
			if(authorByEmail.isPresent()) throw new AlreadyExistsException(HandleException.AuthorAlreadyExistsException.getMessage());
		
			authorByName = getAuthorByName(author.getAuthorname());
			if(authorByName.isPresent()) throw new AlreadyExistsException(HandleException.AuthorAlreadyExistsException.getMessage());
		}else {
			authorByEmail = authorRepository.findAuthorByEmailIdNot(author.getEmail(),author.getAuthorid());
			if(authorByEmail.isPresent()) throw new AlreadyExistsException(HandleException.AuthorAlreadyExistsException.getMessage());
		
			authorByName =  authorRepository.findAuthorByAuthornameIdNot(author.getAuthorname(),author.getAuthorid());
			if(authorByName.isPresent()) throw new AlreadyExistsException(HandleException.AuthorAlreadyExistsException.getMessage());
		}
		authorRepository.save(author);
	}
	/** {@inheritDoc} */
	@Override
	public  Optional<Author> getAuthorById(int authorid) {
		return authorRepository.findById(authorid);
	}
	/** {@inheritDoc} */
	@Override
	public  Optional<Author> getAuthorByEmail(String email) {
		return authorRepository.findByEmail(email);
//		List<Author> allAuthors = getAllAuthors();
//		return allAuthors.stream().filter(author->author.getEmail().equals(email)).findFirst();
	}
	/** {@inheritDoc} */
	@Override
	public void checkAvailability(Optional<Author> author) {
		if(author.isEmpty()) throw new NotFoundException(HandleException.AuthorNotFoundException.getMessage());
	}
	/** {@inheritDoc} */
	@Override
	public Optional<Author> getAuthorByName(String name) {
		return authorRepository.findByAuthorname(name);
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByAuthorid(int authorid) {
		authorRepository.deleteById(authorid);
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByAuthorName(String authorname) {
		authorRepository.deleteByAuthorname(authorname);
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByEmail(String email) {
		authorRepository.deleteByEmail(email);
	}
	/**
	 * Partially updates an author using reflection.
	 * Updating 'books' is not allowed. Duplicate name/email checks are enforced.
	 *
	 * @param data   map of field names to new values
	 * @param author the author entity to update
	 * @throws AlreadyExistsException  if name or email conflicts with another author
	 * @throws IllegalArgumentException if an invalid field is provided
	 */
	@Override
	public void partailUpdate(HashMap<String,Object> data,Author author) {
		data.entrySet().stream().forEach((e)->{
			switch(e.getKey()) {
			case "books" -> {
				throw new IllegalArgumentException(HandleException.IllegalData.getMessage());
			}
			case "authorname" ->{
				Optional<Author> authorByName = authorRepository.findAuthorByAuthornameIdNot((String)e.getValue(), author.getAuthorid());
				if(authorByName.isPresent()) throw new AlreadyExistsException(HandleException.AuthorAlreadyExistsException.getMessage());
			}
			case "email" ->{
				Optional<Author> authorByEmail = authorRepository.findAuthorByEmailIdNot((String)e.getValue(), author.getAuthorid());
				if(authorByEmail.isPresent()) throw new AlreadyExistsException(HandleException.AuthorAlreadyExistsException.getMessage());
			}
			}
			try {
				Field field= ReflectionUtils.findField(Author.class,e.getKey());
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, author,e.getValue());
			}catch(Exception ex) {
				throw new IllegalArgumentException(HandleException.IllegalData.getMessage());
			}
		});
		authorRepository.save(author);
	}
}
