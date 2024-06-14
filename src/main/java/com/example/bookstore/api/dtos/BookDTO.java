package com.example.bookstore.api.dtos;

import com.example.bookstore.api.entities.Author;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class BookDTO {
    private String isbn;
    private String title;
    private List<Author> authors;
    private int year;
    private int price;
    private String genre;
}
