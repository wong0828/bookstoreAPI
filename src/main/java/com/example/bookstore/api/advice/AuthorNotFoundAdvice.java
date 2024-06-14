package com.example.bookstore.api.advice;

import com.example.bookstore.api.exceptions.AuthorNotFoundException;
import com.example.bookstore.api.exceptions.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthorNotFoundAdvice {
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String authorNotFoundHandler(AuthorNotFoundException ex) {
        return ex.getMessage();
    }
}
