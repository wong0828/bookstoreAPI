package com.example.bookstore.api.service;

import com.example.bookstore.api.entities.Book2Author;
import com.example.bookstore.api.repositories.Book2AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class Book2AuthorService {

    @Autowired
    private Book2AuthorRepository book2AuthorRepository;

    public Map<String, List<Long>> getAllBook2AuthorsByBooks() {
        List<Book2Author> all = book2AuthorRepository.findAll();
        return convertToMapIsbnAuthorId(all);
    }

    private static Map<String, List<Long>> convertToMapIsbnAuthorId(List<Book2Author> all) {
        Map<String, List<Long>> hashMap = new HashMap<>();
        for (Book2Author book2Author : all) {
            if (hashMap.containsKey(book2Author.getBookId())) {
                List<Long> authorIds = new ArrayList<>(hashMap.get(book2Author.getBookId()));
                authorIds.add(book2Author.getAuthorId());
                hashMap.put(book2Author.getBookId(), authorIds);
            } else {
                hashMap.put(book2Author.getBookId(), List.of(book2Author.getAuthorId()));
            }
        }
        return hashMap;
    }

    public Map<String, List<Long>> getAllBook2AuthorsByBookIsbn(List<String> isbn) {
        List<Book2Author> all = book2AuthorRepository.findByBookIdIn(isbn);
        return convertToMapIsbnAuthorId(all);
    }

    @Transactional
    public void deleteBook2AuthorsByBookIsbn(String isbn) {
        book2AuthorRepository.deleteByBookId(isbn);
    }

    @Transactional
    public Book2Author saveBook2Author(Book2Author book2Author) {
        return book2AuthorRepository.save(book2Author);
    }

    @Transactional
    public List<Book2Author> saveAll(List<Book2Author> book2Authors) {
        return book2AuthorRepository.saveAll(book2Authors);
    }

    public Map<Long, List<String>> getAllBook2AuthorsByAuthorId(List<Long> authorIds) {
        List<Book2Author> byAuthorId = book2AuthorRepository.findAllByAuthorIdIn(authorIds);
        Map<Long, List<String>> hashMap = new HashMap<>();
        for (Book2Author book2Author : byAuthorId) {
            if (hashMap.containsKey(book2Author.getAuthorId())) {
                List<String> bookIds = new ArrayList<>(hashMap.get(book2Author.getAuthorId()));
                bookIds.add(book2Author.getBookId());
                hashMap.put(book2Author.getAuthorId(), bookIds);
            } else {
                hashMap.put(book2Author.getAuthorId(), List.of(book2Author.getBookId()));
            }
        }
        return hashMap;
    }
}
