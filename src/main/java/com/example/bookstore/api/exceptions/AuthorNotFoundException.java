package com.example.bookstore.api.exceptions;

import java.util.List;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id) {
        super("Author ID " + id + " not found");
    }

    public AuthorNotFoundException(List<Long> ids) {
        super("Author ID " + ids.toString() + " not found");
    }
}
