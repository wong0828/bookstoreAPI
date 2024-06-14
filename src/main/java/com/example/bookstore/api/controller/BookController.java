package com.example.bookstore.api.controller;

import com.example.bookstore.api.dtos.BookDTO;
import com.example.bookstore.api.entities.Book;
import com.example.bookstore.api.exceptions.AuthorNotFoundException;
import com.example.bookstore.api.exceptions.BookAlreadyExistException;
import com.example.bookstore.api.exceptions.BookNotFoundException;
import com.example.bookstore.api.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        try {
            return new ResponseEntity<>(bookService.saveBook(bookDTO), HttpStatus.CREATED);
        } catch (AuthorNotFoundException | BookNotFoundException | BookAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating book with ISBN " + bookDTO.getIsbn(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/book/{isbn}")
    public ResponseEntity<?> updateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        try {
            bookDTO.setIsbn(isbn);
            return new ResponseEntity<>(bookService.updateBook(isbn, bookDTO), HttpStatus.OK);
        } catch (AuthorNotFoundException | BookNotFoundException | BookAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating book with ISBN " + isbn, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/book/{isbn}")
    public Book findBookById(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn);
    }

    @GetMapping(value = "/book")
    public List<BookDTO> findBooks(@RequestParam() String title, @RequestParam(required = false) String author) {
        return bookService.findBooks(title, author);
    }

    @DeleteMapping(value = "/books/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        try {
            bookService.deleteBook(isbn);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting book with ISBN " + isbn, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books")
    public List<BookDTO> all() {
        return bookService.getAllBooks();
    }
}
