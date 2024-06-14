package com.example.bookstore.api.service;

import com.example.bookstore.api.dtos.AuthorDTO;
import com.example.bookstore.api.entities.Author;
import com.example.bookstore.api.entities.Book;
import com.example.bookstore.api.exceptions.AuthorNotFoundException;
import com.example.bookstore.api.repositories.AuthorRepository;
import com.example.bookstore.api.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private Book2AuthorService book2AuthorService;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<Author> findByIds(List<Long> ids) {
        List<Author> authorList = authorRepository.findAllById(ids);
        if (ids.size() != authorList.size()) {
            List<Long> missingIds =  new ArrayList<>();
            for (Long id : ids) {
                if (!authorList.contains(id)) {
                    missingIds.add(id);
                }
            }
            LOGGER.error("Some authors were not found: {}", missingIds);
            throw new AuthorNotFoundException(missingIds);
        }
        return authorList;
    }

    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> findAuthorByName(String name) {
        return authorRepository.findByNameEquals(name);
    }

    public List<AuthorDTO> findByName(String name) {
        List<Author> byNameEquals = authorRepository.findByNameEquals(name);
        Map<Long, List<String>> map = book2AuthorService.getAllBook2AuthorsByAuthorId(byNameEquals.stream().map(Author::getId).toList());
        List<Book> allByIsbn = bookRepository.findAllByIsbn(map.values().stream().flatMap(List::stream).toList());
        return byNameEquals.stream().map(author -> AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .birthday(author.getBirthday())
                .books(map.get(author.getId()).stream().map(isbn -> allByIsbn.stream().filter(book -> book.getIsbn().equals(isbn)).findFirst().orElse(null)).collect(Collectors.toSet()))
                .build()).toList();
    }

}
