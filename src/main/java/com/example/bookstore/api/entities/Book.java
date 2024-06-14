package com.example.bookstore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column(name = "isbn", unique = true)
    private String isbn;
    private String title;
    @Column(name = "\"year\"")
    private int year;
    private int price;
    private String genre;
}
