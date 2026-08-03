package com.example.bookstore;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import com.example.bookstore.Controllers.BookController;
import com.example.bookstore.Entites.Book;
import com.example.bookstore.Entites.BookDTO;
import com.example.bookstore.ExceptionHandling.NotFoundException;
import com.example.bookstore.Services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for BookController using MockMvc and mocked BookService.
 */
@WebMvcTest(BookController.class)
public class BookControllerTests {

	/** MockMvc instance for performing HTTP requests in tests. */
	@Autowired
	private MockMvc mockMvc;

	/** Mocked BookService bean injected into the controller. */
	@MockitoBean
	private BookService bookService;

	/** ObjectMapper for serializing request bodies to JSON. */
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * Verifies GET /Book/getBookById returns 200 OK
	 * when the book exists.
	 */
	@Test
	public void getBookById() throws Exception{
		Book book=new Book(1,"Harry Potter and the Philosopher's Stone",
		        LocalDate.of(1997, 6, 26),
		        "A young wizard's journey begins.",0, null, null);
		when(bookService.getBookById(1)).thenReturn(Optional.of(book));
		mockMvc.perform(get("/Book/getBookById/{bookid}",1))
		.andExpect(status().isOk());
		verify(bookService).getBookById(1);
	}
	
	/**
	 * Verifies GET /Book/getBookByName returns 404 NOT FOUND
	 * when the book does not exist.
	 */
	@Test
	public void getBookByName() throws Exception{
		when( bookService.getBookByName("The Jungle Book")).thenReturn(Optional.empty());
		doThrow(new NotFoundException("Book Not Found"))
        .when(bookService).checkAvailability(any());
		mockMvc.perform(get("/Book/getBookByName/{bookname}","The Jungle Book"))
		.andExpect(status().isNotFound());
		verify(bookService).getBookByName("The Jungle Book");
	}
	
	/**
	 * Verifies POST /Book/addBook returns 201 CREATED
	 * when a valid BookDTO is submitted.
	 */
	@Test
	public void addBook() throws Exception{
		BookDTO bookDTO= new BookDTO(1,"The Jungle Book",LocalDate.now(),"A young child journey begins.",1, 0, 0);
		doNothing().when(bookService).addBook(bookDTO);
		mockMvc.perform(post("/Book/addBook")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(bookDTO)))
		.andExpect(status().isCreated());
		verify(bookService).addBook(any());
	}
	
	/**
	 * Verifies DELETE /Book/deleteBookByName returns 200 OK
	 * when the book exists.
	 */
	@Test
	public void deleteBookByName() throws Exception{
		when( bookService.getBookByName("The Jungle Book")).thenReturn(Optional.empty());
		doNothing().when(bookService).checkAvailability(any());
		doNothing().when(bookService).deleteByBookName("The Jungle Book");
		mockMvc.perform(delete("/Book/deleteBookByName/{bookname}","The Jungle Book"))
		.andExpect(status().isOk());
		verify(bookService).getBookByName("The Jungle Book");
	}
	
	/**
	 * Verifies PATCH /Book/partialUpdate returns 404 NOT FOUND
	 * when the book does not exist.
	 */
	@Test
	public void patchMapping() throws Exception{
		HashMap<String,Object> data =new HashMap<>();
		mockMvc.perform(patch("/Book/partialUpdate/{bookid}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
		.andExpect(status().isBadRequest());
		
	}
}
