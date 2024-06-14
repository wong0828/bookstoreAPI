package com.example.bookstore.api.exceptions;

public class BookAlreadyExistException extends RuntimeException {
    public BookAlreadyExistException(String isbn) {
        super("Book with ISBN " + isbn + " already exists");
    }
}
