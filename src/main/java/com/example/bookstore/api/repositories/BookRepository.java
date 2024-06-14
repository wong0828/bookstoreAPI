package com.example.bookstore.api.repositories;

import com.example.bookstore.api.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findByIsbn(String isbn);

    void deleteByIsbn(String isbn);

    List<Book> findByTitleEquals(String title);

    @Query("select b from Book b where b.isbn in :isbns")
    List<Book> findAllByIsbn(List<String> isbns);
}
