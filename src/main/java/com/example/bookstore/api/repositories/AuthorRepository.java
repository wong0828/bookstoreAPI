package com.example.bookstore.api.repositories;

import com.example.bookstore.api.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameEquals(String name);
}
