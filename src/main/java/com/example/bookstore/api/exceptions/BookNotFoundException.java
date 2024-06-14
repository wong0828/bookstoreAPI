package com.example.bookstore.api.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Book ID " + isbn + " not found.");
    }
}
