package com.example.bookstore.api.controller;

import com.example.bookstore.api.dtos.AuthorDTO;
import com.example.bookstore.api.dtos.BookDTO;
import com.example.bookstore.api.entities.Author;
import com.example.bookstore.api.entities.Book;
import com.example.bookstore.api.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping(value = "/author")
    public Author createAuthor(@RequestBody AuthorDTO authorDTO) {
        Author author = Author.builder()
                .id(authorDTO.getId())
                .name(authorDTO.getName())
                .birthday(authorDTO.getBirthday())
//                .books(authorDTO.getBookIds().stream().map(
//                        id -> Book.builder()
//                                .id(id)
//                                .build()
//                ).collect(Collectors.toSet()))
                .build();
        return authorService.saveAuthor(author);
    }

    @GetMapping(value = "/author")
    public List<AuthorDTO> findByAuthor(@RequestParam() String author) {
        return authorService.findByName(author);
    }


    @GetMapping(value = "/authors")
    public List<Author> all() {
        return authorService.getAllAuthors();
    }
}
