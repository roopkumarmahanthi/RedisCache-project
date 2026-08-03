package com.example.bookstore.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.Entites.Publisher;
import com.example.bookstore.Services.PublisherService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * REST controller for managing Publisher resources.
 * Provides endpoints for CRUD operations on publishers.
 */
@RestController
@RequestMapping("/Publisher")
@Validated
public class PublisherController {
	
	@Autowired
	private PublisherService publisherService;
	
	/**
	 * Retrieves all publishers.
	 *
	 * @return ResponseEntity containing a list of all publishers
	 */
	@GetMapping("/allPublishers")
	public ResponseEntity<Map<String,Object>> allPublishers(){
		List<Publisher> allPublishers = publisherService.getAllPublishers();
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Publishers",allPublishers));
	}
	
	/**
	 * Retrieves a publisher by their ID.
	 *
	 * @param publisherid the ID of the publisher to retrieve
	 * @return ResponseEntity containing the matched publisher
	 */
	@GetMapping("/getPublisherById/{publisherid}")
	public ResponseEntity<Map<String,Object>> getPublisher(@PathVariable int publisherid){
		Optional<Publisher> publisherById = publisherService.getPublisherById(publisherid);
		publisherService.checkAvailability(publisherById);
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Publisher",publisherById));
	}
	
	/**
	 * Retrieves a publisher by their name.
	 *
	 * @param name the name of the publisher (must not be blank)
	 * @return ResponseEntity containing the matched publisher
	 */
	@GetMapping("/getPublisherByName/{name}")
	public ResponseEntity<Map<String,Object>> getPublisherByName(@PathVariable @NotBlank String name){
		Optional<Publisher> publisherByName = publisherService.getPublisherByName(name);
		publisherService.checkAvailability(publisherByName);
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Publisher",publisherByName));
	}
	
	/**
	 * Retrieves a publisher by their email address.
	 * Only gmail.com, gmail.in, yahoo.com, yahoo.in are accepted.
	 *
	 * @param email the valid email address of the publisher
	 * @return ResponseEntity containing the matched publisher
	 */
	@GetMapping("/getPublisherByEmail/{email}")
	public ResponseEntity<Map<String,Object>> getPublisherByEmail(@PathVariable 
			@Email( regexp = "(?i)^[A-Za-z0-9._+%-]+@(gmail|yahoo)\\.(com|in)$"
			,message = "Please give valid email. Only gmail.com, gmail.in, yahoo.com, yahoo.in are allowed.")
			String email){
		Optional<Publisher> publisherByEmail = publisherService.getPublisherByEmail(email);
		publisherService.checkAvailability(publisherByEmail);
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(Map.of("Publisher",publisherByEmail));
	}
	
	/**
	 * Adds a new publisher.
	 *
	 * @param publisher the publisher object to be created (validated)
	 * @return ResponseEntity with a success message
	 */
	@PostMapping("/addPublisher")
	public ResponseEntity<Map<String,Object>> addPublisher(@Valid @RequestBody Publisher publisher){
		publisherService.addPublisher(publisher);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("Message","publisher added succussfully"));
	}
	
	/**
	 * Fully updates an existing publisher.
	 *
	 * @param publisher the publisher object with updated fields (validated)
	 * @return ResponseEntity with a success message
	 */
	@PutMapping("/updatePublisher")
	public ResponseEntity<Map<String,Object>> updatePublisher(@Valid @RequestBody Publisher publisher){
		Optional<Publisher> publisherById = publisherService.getPublisherById(publisher.getPublisherid());
		publisherService.checkAvailability(publisherById);
		publisherService.addPublisher(publisher);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("Message","publisher updated succussfully"));
	}
	
	/**
	 * Partially updates an existing publisher's fields.
	 *
	 * @param data        a map of fields to update
	 * @param publisherid the ID of the publisher to partially update
	 * @return ResponseEntity with a success message, or 400 if data is empty
	 */
	@PatchMapping("/partialUpdate/{publisherid}")
	public ResponseEntity<Map<String,Object>> partialUpdate(@RequestBody HashMap<String,Object> data,@PathVariable int publisherid){
//		if(data.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(Map.of("message","please give valid data"));
//		}
		Optional<Publisher> publisherById = publisherService.getPublisherById(publisherid);
		publisherService.checkAvailability(publisherById);
		publisherService.partialUpdate(data, publisherById.get());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("Message","publisher updated succussfully"));
	}
	
	/**
	 * Deletes a publisher by their ID.
	 *
	 * @param publisherid the ID of the publisher to delete
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deletePublisherById/{publisherid}")
	public ResponseEntity<Map<String,Object>> deletePublisherById(@PathVariable int publisherid){
		Optional<Publisher> publisherById = publisherService.getPublisherById(publisherid);
		publisherService.checkAvailability(publisherById);
		publisherService.deleteByPublisherId(publisherid);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","Publisher deleted succussfully"));
	}
	
	/**
	 * Deletes a publisher by their name.
	 *
	 * @param publishername the name of the publisher to delete (must not be blank)
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deletePublisherByName/{publishername}")
	public ResponseEntity<Map<String,Object>> deletePublisherByName(@PathVariable @NotBlank String publishername){
		Optional<Publisher> publisherByName = publisherService.getPublisherByName(publishername);
		publisherService.checkAvailability(publisherByName);
		publisherService.deleteByPublisherName(publishername);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","Publisher deleted succussfully"));
	}
	
	/**
	 * Deletes a publisher by their email address.
	 * Only gmail.com, gmail.in, yahoo.com, yahoo.in are accepted.
	 *
	 * @param email the valid email address of the publisher to delete
	 * @return ResponseEntity with a success message
	 */
	@DeleteMapping("/deletePublisherByEmail")
	public ResponseEntity<Map<String,Object>> deletePublisherByEmail(@RequestParam 
			@Email( regexp = "(?i)^[A-Za-z0-9._+%-]+@(gmail|yahoo)\\.(com|in)$"
			,message = "Please give valid email. Only gmail.com, gmail.in, yahoo.com, yahoo.in are allowed.")
			String email){
		Optional<Publisher> publisherByEmail = publisherService.getPublisherByEmail(email);
		publisherService.checkAvailability(publisherByEmail);
		publisherService.deleteByPublisherEmail(email);
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("Message","Publisher deleted succussfully"));
	}
}
