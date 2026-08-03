package com.example.bookstore.Enums;


/**
 * Enum holding standardized exception messages for the bookstore application.
 * Used across service and controller layers to ensure consistent error messaging.
 */
public enum HandleException {
    BookAlreadyExistsException("Given Book details is already present in database. Please maintain these constrains:\n The book must me UNIQUE.\n The authorid, publisherid should be NOT NULL"),
    BookNotFoundException("The Book with the given details is not present in database. Please give Valid details"),
    
    AuthorAlreadyExistsException("Given Author details is already present in database. Please maintain these constrains:\n The Author must me UNIQUE.\n The authorname, email should be UNIQUE"),
    AuthorNotFoundException("The Author with given details is not present in database. Please give Valid details"),
    
    PublisherAlreadyExistsException("Given Publisher details is already present in database. Please maintain these constrains:\n The Publisher must me unique.\n The publishername, email should be UNIQUE"),
    PublisherNotFoundException("The Publisher with given details is not present in database. Please give Valid details"),
    
    IllegalData("Given fields are not valid. Please give valid fields");
	private String message;
	private HandleException(String message) {
		this.message=message;
	}
	/** Returns the error message associated with this exception type. */
	public String getMessage() {
		return message;
	}
	/** Sets the error message for this exception type. */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
