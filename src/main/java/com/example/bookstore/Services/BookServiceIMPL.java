package com.example.bookstore.Services;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.example.bookstore.Entites.Author;
import com.example.bookstore.Entites.Book;
import com.example.bookstore.Entites.BookDTO;
import com.example.bookstore.Entites.Publisher;
import com.example.bookstore.Enums.HandleException;
import com.example.bookstore.ExceptionHandling.AlreadyExistsException;
import com.example.bookstore.ExceptionHandling.NotFoundException;
import com.example.bookstore.Repositories.BookRepository;

/**
 * Implementation of BookService.
 * Handles book persistence, duplicate checks, author/publisher validation,
 * partial updates via reflection, and DTO conversions.
 */
@Service
public class BookServiceIMPL implements BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	
	public String generateKey(int key) {
		return "Bookid:"+key;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	/** {@inheritDoc} */
	@Override
	public void deleteByBookName(String bookname) {
		bookRepository.deleteByBookname(bookname);
	}

	/**
	 * Creates or updates a book from the given DTO.
	 * Validates that the author and publisher exist before saving.
	 *
	 * @param bookDto the book data transfer object
	 * @throws AlreadyExistsException if a book with the same name already exists
	 * @throws NotFoundException      if the author or publisher is not found
	 */
	@Override
	public void addBook(BookDTO bookDto) {
		Optional<Book> byBookname =null;
		if(bookDto.getBookid()==0) {
				byBookname = bookRepository.findByBookname(bookDto.getBookname());
		}else {
			byBookname = bookRepository.findById(bookDto.getBookid());
		}
		if(byBookname.isPresent()) throw new AlreadyExistsException(HandleException.BookAlreadyExistsException.getMessage());
		
		Optional<Author> authorById = authorService.getAuthorById(bookDto.getAuthorid());
		if(authorById.isEmpty()) throw new NotFoundException(HandleException.AuthorNotFoundException.getMessage());

		Optional<Publisher> publisherById = publisherService.getPublisherById(bookDto.getPublisherid());
		if(!publisherById.isPresent()) throw new NotFoundException(HandleException.PublisherNotFoundException.getMessage());
		
		Book book = BookDTOtoBook(bookDto);
		authorById.get().getBooks().add(book);
		publisherById.get().getBooks().add(book);
		
		Book save = bookRepository.save(book);
//		redisTemplate.opsForValue().set(generateKey(save.getBookid()), save);
	}
	/** {@inheritDoc} */
	@Override
	public Optional<Book> getBookById(int bookid) {
		Book cachedBook=(Book)redisTemplate.opsForValue().get(generateKey(bookid));
		if(cachedBook!=null) {
			System.out.println("cached from redis");
			return Optional.of(cachedBook);
		}
		System.out.println("cached from db");
		Optional<Book> byId = bookRepository.findById(bookid);
		checkAvailability(byId);
		redisTemplate.opsForValue().set(generateKey(byId.get().getBookid()), byId.get());
		return byId;
	}
	/** {@inheritDoc} */
	@Override
	public Optional<Book> getBookByName(String bookname) {
		return bookRepository.findByBookname(bookname);
	}
	/** {@inheritDoc} */
	@Override
	public void deleteBookById(int bookid) {
		bookRepository.deleteById(bookid);
	}
	/** {@inheritDoc} */
	@Override
	public void checkAvailability(Optional<Book> book) {
		if(!book.isPresent()) throw new NotFoundException(HandleException.BookNotFoundException.getMessage());
	}
	/**
	 * Partially updates a book using reflection on BookDTO.
	 * Validates bookname uniqueness, and author/publisher existence for those fields.
	 *
	 * @param data the map of field names to new values
	 * @param book the book entity to update
	 * @throws AlreadyExistsException  if bookname conflicts with another book
	 * @throws NotFoundException       if the given authorid or publisherid does not exist
	 * @throws IllegalArgumentException if an invalid field is provided
	 */
	@Override
	public void partialUpdate(HashMap<String,Object> data,Book book) {
		BookDTO bookToBookDTO = BookToBookDTO(book);
		
		data.entrySet().stream().forEach((e)->{
			switch(e.getKey()) {
			case "bookname" ->{
				Optional<Book> bookByBooknameIdNot = bookRepository.findBookByBooknameIdNot((String)e.getValue(), book.getBookid());
				if(bookByBooknameIdNot.isPresent()) throw new AlreadyExistsException(HandleException.BookAlreadyExistsException.getMessage()); 
			}
			case "authorid" ->{
				Optional<Author> authorById = authorService.getAuthorById((int)e.getValue());
				if(authorById.isEmpty()) throw new NotFoundException(HandleException.AuthorNotFoundException.getMessage());
			}
			case "publisherid" ->{
				Optional<Publisher> publisherById = publisherService.getPublisherById((int)e.getValue());
				if(!publisherById.isPresent()) throw new NotFoundException(HandleException.PublisherNotFoundException.getMessage());
			}
			}
			
			try {
				Field field= ReflectionUtils.findField(BookDTO.class,e.getKey());
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, bookToBookDTO,e.getValue());
			}catch(Exception ex) {
				throw new IllegalArgumentException(HandleException.IllegalData.getMessage());
			}
		});
		
		Book bookDTOtoBook = BookDTOtoBook(bookToBookDTO);
		bookRepository.save(bookDTOtoBook);
	}
	/** {@inheritDoc} */
	@Override
	public  List<Book> getBookByPriceGreaterThanEqual(int price) {
		List<Book> allBooks = getAllBooks();
		return allBooks.stream().filter(b-> b.getPrice()>=price).collect(Collectors.toList());
	}
	/** {@inheritDoc} */
	@Override
	public List<Book> getBooksByAuthorName(String authorname) {
		return bookRepository.findBooksByAuthorName(authorname);
	}
	/** {@inheritDoc} */
	@Override
	public List<Book> getBooksByPublisherName(String publishername) {
		return bookRepository.findBooksByPublisherName(publishername);
	}
	/**
	 * Converts a BookDTO to a Book entity, resolving author and publisher by ID.
	 *
	 * @param bookDto the DTO to convert
	 * @return the corresponding Book entity
	 */
	public Book BookDTOtoBook(BookDTO bookDto) {
		Book book=new Book(bookDto.getBookid(),
				bookDto.getBookname(),
				LocalDate.now(),
				bookDto.getDescription(),
				bookDto.getPrice(),
				authorService.getAuthorById(bookDto.getAuthorid()).get(),
				publisherService.getPublisherById(bookDto.getPublisherid()).get());
		return book;
	}

	/**
	 * Converts a Book entity to a BookDTO.
	 *
	 * @param book the entity to convert
	 * @return the corresponding BookDTO
	 */
	public BookDTO BookToBookDTO(Book book) {
		BookDTO bookDto= new BookDTO(book.getBookid(),
				book.getBookname(),
				book.getDate(),
				book.getDescription(),
				book.getPrice(),
				book.getAuthor().getAuthorid(),
				book.getPublisher().getPublisherid());
		return bookDto;
	}

}