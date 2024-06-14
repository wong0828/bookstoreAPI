package com.example.bookstore.api.repositories;

import com.example.bookstore.api.entities.Book2Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Book2AuthorRepository extends JpaRepository<Book2Author, Long> {

    public List<Book2Author> findByBookIdIn(List<String> bookId);
    List<Book2Author> findByAuthorId(Long authorId);

    List<Book2Author> findAllByAuthorIdIn(List<Long> authorIds);
    void deleteByBookId(String bookId);
}
