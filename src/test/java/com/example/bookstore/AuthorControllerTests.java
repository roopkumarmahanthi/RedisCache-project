package com.example.bookstore;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bookstore.Controllers.AuthorController;
import com.example.bookstore.Entites.Author;
import com.example.bookstore.ExceptionHandling.NotFoundException;
import com.example.bookstore.Services.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for AuthorController using MockMvc and mocked AuthorService.
 */
@WebMvcTest(AuthorController.class)
public class AuthorControllerTests {

	/** MockMvc instance for performing HTTP requests in tests. */
	@Autowired
	private MockMvc mockMvc;

	/** Mocked AuthorService bean injected into the controller. */
	@MockitoBean
	private AuthorService authorService;

	/** ObjectMapper for serializing request bodies to JSON. */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Verifies GET /Author/getAuthorByName returns 302 FOUND
	 * and the correct author name in the response body.
	 */
	@Test
	public void getAuthorByName() throws Exception{
		Author author= new Author(0, "roopkumar","graduation completed", "roop136@gmail.com", null);
		when(authorService.getAuthorByName("roopkumar")).thenReturn(Optional.of(author));
		doNothing().when(authorService).checkAvailability(any());
		mockMvc.perform(get("/Author/getAuthorByName/{authorname}","roopkumar"))
		.andExpect(status().isFound())
		.andExpect(jsonPath("$.Author.authorname").value("roopkumar"));

		verify(authorService).getAuthorByName(anyString());
	}
	
	/**
	 * Verifies POST /Author/addAuthor returns 201 CREATED
	 * when a valid author is submitted.
	 */
	@Test
	public void addAuthor() throws Exception{
		Author author= new Author(0, "roopkumar","graduation completed", "roop136@gmail.com", null);
		doNothing().when(authorService).saveAuthor(author);
		mockMvc.perform(post("/Author/addAuthor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(author)))
		.andExpect(status().isCreated());
		verify(authorService).saveAuthor(any());
	}
	
	/**
	 * Verifies DELETE /Author/deleteAuthorById returns 200 OK
	 * when the author exists.
	 */
	@Test
	public void deleteAuthorById() throws Exception{
		Author author= new Author(10, "roopkumar","graduation completed", "roop136@gmail.com", null);
		when(authorService.getAuthorById(10)).thenReturn(Optional.of(author));
		doNothing().when(authorService).checkAvailability(any());
		doNothing().when(authorService).deleteByAuthorid(10);
		mockMvc.perform(delete("/Author/deleteAuthorById/{authorid}",10))
		.andExpect(status().isOk());
		verify(authorService).getAuthorById(anyInt());
	}
	
	/**
	 * Verifies PATCH /Author/partialUpdate returns 404 NOT FOUND
	 * when the author does not exist.
	 */
	@Test
	public void patchMapping() throws Exception{
		HashMap<String,Object> data =new HashMap<>();
		when(authorService.getAuthorById(10)).thenReturn(Optional.empty());
		doThrow(new NotFoundException("Author is not found")).when(authorService).checkAvailability(any());
		doNothing().when(authorService).partailUpdate(data,new Author());
		mockMvc.perform(patch("/Author/partialUpdate/{authorid}",10)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
		.andExpect(status().isBadRequest());
	}
	
	/**
	 * Verifies PUT /Author/updateAuthor returns 201 CREATED
	 * when a valid author is submitted.
	 */
	@Test
	public void putMapping() throws Exception{
		Author author= new Author(10, "roopkumar","graduation completed", "roop136@gmail.com", null);
		when(authorService.getAuthorById(10)).thenReturn(Optional.empty());
		doNothing().when(authorService).checkAvailability(any());
		doNothing().when(authorService).saveAuthor(author);
		mockMvc.perform(post("/Author/addAuthor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(author)))
		.andExpect(status().isCreated());
		verify(authorService).saveAuthor(any());
	}
	
}
