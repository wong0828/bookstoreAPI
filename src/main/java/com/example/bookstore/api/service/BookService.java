package com.example.bookstore.api.service;

import com.example.bookstore.api.dtos.BookDTO;
import com.example.bookstore.api.entities.Author;
import com.example.bookstore.api.entities.Book;
import com.example.bookstore.api.entities.Book2Author;
import com.example.bookstore.api.exceptions.BookAlreadyExistException;
import com.example.bookstore.api.exceptions.BookNotFoundException;
import com.example.bookstore.api.repositories.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private Book2AuthorService book2AuthorService;

    @Autowired
    private AuthorService authorService;

    @Transactional
    public BookDTO saveBook(BookDTO bookDTO) {
        Book byIsbn = findByIsbn(bookDTO.getIsbn());
        if (byIsbn != null) {
            log.error("Book with ISBN {} already exists", bookDTO.getIsbn());
            throw new BookAlreadyExistException(bookDTO.getIsbn());
        }
        Book saved = bookRepository.save(Book.builder()
                        .price(bookDTO.getPrice())
                        .isbn(bookDTO.getIsbn())
                        .genre(bookDTO.getGenre())
                        .title(bookDTO.getTitle())
                        .year(bookDTO.getYear())
                .build());
        List<Book2Author> book2AuthorList = new ArrayList<>();
        for (Author author : bookDTO.getAuthors()) {
            book2AuthorList.add(Book2Author.builder()
                    .bookId(saved.getIsbn())
                    .authorId(author.getId())
                    .build());
        }
        List<Book2Author> savedBook2AuthorList = book2AuthorService.saveAll(book2AuthorList);
        List<Author> authors = authorService.findByIds(savedBook2AuthorList.stream().map(Book2Author::getAuthorId).collect(Collectors.toList()));
        bookDTO.setIsbn(saved.getIsbn());
        bookDTO.setAuthors(authors);
        return bookDTO;
    }

    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<BookDTO> findBooks(String title, String author) {
        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author)) {
            // title & author
            List<BookDTO> bookDTOS = searchByBookTitle(title);
            List<Author> byAuthorName = authorService.findAuthorByName(author);
            List<BookDTO> resultList = new ArrayList<>();
            for (BookDTO bookDTO : bookDTOS) {
                for (Author bookDTOAuthor : bookDTO.getAuthors()) {
                    if (byAuthorName.contains(bookDTOAuthor)) {
                        resultList.add(bookDTO);
                        break;
                    }
                }
            }
            return resultList;
        } else if (StringUtils.isNotBlank(title)) {
            // title
            return searchByBookTitle(title);
        } else {
            // thr error
            log.error("Title and Author are blank");
            throw new IllegalArgumentException("Title and Author are blank");
        }
    }

    private List<BookDTO> searchByBookTitle(String title) {
        List<Book> byTitleEquals = bookRepository.findByTitleEquals(title);
        Map<String, List<Long>> allBook2AuthorsByBookIsbn = book2AuthorService.getAllBook2AuthorsByBookIsbn(byTitleEquals.stream().map(Book::getIsbn).collect(Collectors.toList()));
        List<Author> authors = authorService.findByIds(allBook2AuthorsByBookIsbn.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
        return mapToBookDTO(byTitleEquals, allBook2AuthorsByBookIsbn, authors);
    }

    private static List<BookDTO> mapToBookDTO(List<Book> bookList, Map<String, List<Long>> bookIsbnAuthorIdsMap, List<Author> authorList) {
        return bookList.stream().map(book -> BookDTO.builder()
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .year(book.getYear())
                .price(book.getPrice())
                .genre(book.getGenre())
                .authors(bookIsbnAuthorIdsMap.get(book.getIsbn()).stream().map(authorId -> authorList.stream().filter(author -> author.getId().equals(authorId)).findFirst().orElse(null)).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public void deleteBook(String isbn) {
        Book byIsbn = findByIsbn(isbn);
        if (byIsbn == null) {
            log.error("Book with ISBN {} does not exists", isbn);
            throw new BookNotFoundException(isbn);
        }
        bookRepository.deleteByIsbn(isbn);
        book2AuthorService.deleteBook2AuthorsByBookIsbn(isbn);
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        Map<String, List<Long>> longSetMap = book2AuthorService.getAllBook2AuthorsByBooks();
        List<Author> authors = authorService.getAllAuthors();

        return books.stream().map(book -> BookDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .price(book.getPrice())
                .year(book.getYear())
                .genre(book.getGenre())
                .authors(
                        longSetMap.get(book.getIsbn()).stream().map(authorId -> authors.stream().filter(author -> author.getId().equals(authorId)).findFirst().orElse(null)).collect(Collectors.toList())
                )
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public BookDTO updateBook(String isbn, BookDTO bookDTO) {
        Book byIsbn = findByIsbn(bookDTO.getIsbn());
        if (byIsbn == null) {
            log.error("Book with ISBN {} does not exists", bookDTO.getIsbn());
            throw new BookNotFoundException(bookDTO.getIsbn());
        }
        Book saved = bookRepository.save(Book.builder()
                .price(bookDTO.getPrice())
                .isbn(bookDTO.getIsbn())
                .genre(bookDTO.getGenre())
                .title(bookDTO.getTitle())
                .year(bookDTO.getYear())
                .build());
        book2AuthorService.deleteBook2AuthorsByBookIsbn(saved.getIsbn());

        List<Book2Author> book2AuthorList = new ArrayList<>();
        for (Author author : bookDTO.getAuthors()) {
            book2AuthorList.add(Book2Author.builder()
                    .bookId(saved.getIsbn())
                    .authorId(author.getId())
                    .build());
        }
        List<Book2Author> savedBook2AuthorList = book2AuthorService.saveAll(book2AuthorList);
        List<Author> authors = authorService.findByIds(savedBook2AuthorList.stream().map(Book2Author::getAuthorId).collect(Collectors.toList()));
        bookDTO.setIsbn(saved.getIsbn());
        bookDTO.setAuthors(authors);
        return bookDTO;
    }

    public List<Book> findAllByIsbn(List<String> bookIds) {
        return bookRepository.findAllByIsbn(bookIds);
    }

}
