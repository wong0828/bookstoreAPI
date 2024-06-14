package com.example.bookstore.api.dtos;

import com.example.bookstore.api.entities.Book;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class AuthorDTO {
    private Long id;
    private String name;
    private LocalDate birthday;
    private Set<Book> books;
}
