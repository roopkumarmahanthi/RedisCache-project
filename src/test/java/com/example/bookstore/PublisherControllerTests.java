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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bookstore.Controllers.PublisherController;
import com.example.bookstore.Entites.Publisher;
import com.example.bookstore.ExceptionHandling.NotFoundException;
import com.example.bookstore.Services.PublisherService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for PublisherController using MockMvc and mocked PublisherService.
 */
@WebMvcTest(PublisherController.class)
public class PublisherControllerTests {

	/** MockMvc instance for performing HTTP requests in tests. */
	@Autowired
	private MockMvc mockMvc;

	/** Mocked PublisherService bean injected into the controller. */
	@MockitoBean
	private PublisherService publisherService;

	/** ObjectMapper for serializing request bodies to JSON. */
	private final ObjectMapper mapper;

	/**
	 * Constructor-based injection of ObjectMapper.
	 *
	 * @param mapper the ObjectMapper to use for JSON serialization
	 */
	@Autowired
	public PublisherControllerTests(ObjectMapper mapper) {
		this.mapper=mapper;
	}
	
	/**
	 * Verifies DELETE /Publisher/deletePublisherById returns 404 NOT FOUND
	 * when the publisher does not exist.
	 */
	@Test
	public void deletePublisher() throws Exception{
		when(publisherService.getPublisherById(1)).thenReturn(Optional.empty());
		doThrow(new NotFoundException("Publisher Not Found"))
		.when(publisherService).checkAvailability(any());
		mockMvc.perform(delete("/Publisher/deletePublisherById/{publisherid}",1))
		.andExpect(status().isNotFound());
		verify(publisherService).getPublisherById(anyInt());
	}
	
	/**
	 * Verifies GET /Publisher/getPublisherByEmail returns 302 FOUND
	 * when the publisher exists.
	 */
	@Test
	public void getPublisherByEmail() throws Exception{
		Publisher publisher=new Publisher(1, "manikanta", "8464894144", "mani123@gmail.com", null);
		when(publisherService.getPublisherByEmail("mani123@gmail.com")).thenReturn(Optional.of(publisher));
		doNothing().when(publisherService).checkAvailability(any());
		mockMvc.perform(get("/Publisher/getPublisherByEmail/{email}","mani123@gmail.com"))
		.andExpect(status().isFound());
		verify(publisherService).getPublisherByEmail(anyString());
	}
	
	/**
	 * Verifies POST /Publisher/addPublisher returns 201 CREATED
	 * when a valid publisher is submitted.
	 */
	@Test
	public void addPublisher() throws Exception{
		Publisher publisher=new Publisher(1, "manikanta", "8464894144", "mani123@gmail.com", null);
		doNothing().when(publisherService).addPublisher(publisher);
		mockMvc.perform(post("/Publisher/addPublisher")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(publisher)))
		.andExpect(status().isCreated());
		verify(publisherService).addPublisher(any());
	}
	
	/**
	 * Verifies PATCH /Publisher/partialUpdate returns 404 NOT FOUND
	 * when the publisher does not exist.
	 */
	@Test
	public void patchMapping() throws Exception{
		HashMap<String,Object> data= new HashMap<>();
		when(publisherService.getPublisherById(1)).thenReturn(Optional.empty());
		doThrow(new NotFoundException("Publisher Not Found"))
		.when(publisherService).checkAvailability(any());
		doNothing().when(publisherService).partialUpdate(data,new Publisher());
		mockMvc.perform(patch("/Publisher/partialUpdate/{publisherid}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
		.andExpect(status().isNotFound());
		verify(publisherService).getPublisherById(1);
	}
	
	/**
	 * Verifies PUT /Publisher/updatePublisher returns 201 CREATED
	 * when a valid publisher is submitted.
	 */
	@Test
	public void putMapping() throws Exception{
		Publisher publisher=new Publisher(1, "manikanta", "8464894144", "mani123@gmail.com", null);
		when(publisherService.getPublisherById(1)).thenReturn(Optional.of(publisher));
		doNothing().when(publisherService).checkAvailability(any());
		doNothing().when(publisherService).addPublisher(publisher);
		mockMvc.perform(put("/Publisher/updatePublisher")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(publisher)))
		.andExpect(status().isCreated());
		verify(publisherService).addPublisher(any());
	}
}
