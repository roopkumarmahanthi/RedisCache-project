package com.example.bookstore;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bookstore.Entites.Author;
import com.example.bookstore.Entites.BookDTO;
import com.example.bookstore.Entites.Publisher;
import com.example.bookstore.ExceptionHandling.AlreadyExistsException;
import com.example.bookstore.Repositories.AuthorRepository;
import com.example.bookstore.Repositories.BookRepository;
import com.example.bookstore.Repositories.PublisherRepository;
import com.example.bookstore.Services.AuthorServiceIMPL;
import com.example.bookstore.Services.BookServiceIMPL;
import com.example.bookstore.Services.PublisherServiceIMPL;

/**
 * Unit tests for AuthorServiceIMPL, PublisherServiceIMPL, and BookServiceIMPL
 * using Mockito to mock repository dependencies.
 */
@ExtendWith(MockitoExtension.class)
public class AllServicesTests {

	/** Mock repository for author-related database operations. */
	@Mock
	private AuthorRepository authorRepository;

	/** AuthorServiceIMPL instance with mocked AuthorRepository injected. */
	@InjectMocks
	private AuthorServiceIMPL authorServiceIMPL;
	
	/**
	 * Verifies that getAuthorByEmail returns an empty Optional
	 * when no author exists with the given email.
	 */
	@Test
	public void getAuthorByEmail(){
		when(authorRepository.findByEmail("roop136@gmail.com")).thenReturn(Optional.empty());
		Optional<Author> authorByEmail = authorServiceIMPL.getAuthorByEmail("roop136@gmail.com");
		assertTrue(authorByEmail.isEmpty());
		verify(authorRepository).findByEmail(any());
	}
	
	/** Mock repository for publisher-related database operations. */
	@Mock
	private PublisherRepository publisherRepository;

	/** PublisherServiceIMPL instance with mocked PublisherRepository injected. */
	@InjectMocks
	private PublisherServiceIMPL publisherServiceIMPL;
	
	/**
	 * Verifies that getPublisherByName returns the correct publisher
	 * and that the returned name does not match an unrelated name.
	 */
	@Test
	public void getPublisherByName() {
		Publisher publisher=new Publisher(1, "manikanta", "8464894144", "mani123@gmail.com", null);
		when(publisherRepository.findByPublishername("manikanta")).thenReturn(Optional.of(publisher));
		Optional<Publisher> publisherByName = publisherServiceIMPL.getPublisherByName("manikanta");
		assertNotEquals(publisherByName.get().getPublishername(),"roop kumar");
		verify(publisherRepository).findByPublishername(anyString());
	}
	
	/** Mock repository for book-related database operations. */
	@Mock
	private BookRepository bookRepository;

	/** BookServiceIMPL instance with mocked BookRepository injected. */
	@InjectMocks
	private BookServiceIMPL bookServiceIMPL;
	
	/**
	 * Verifies that addBook throws AlreadyExistsException
	 * when the repository signals the book already exists.
	 */
	@Test
	public void addBook() {
		BookDTO bookDTO= new BookDTO(0,"The Jungle Book",LocalDate.now(),"A young child journey begins.",0, 0, 0);
		when(bookRepository.findByBookname(bookDTO.getBookname())).thenThrow(new AlreadyExistsException("Book already Eixsts"));
		assertThrows(AlreadyExistsException.class,()->{
			bookServiceIMPL.addBook(bookDTO);
		});
		verify(bookRepository).findByBookname(anyString());
	}
	
}
