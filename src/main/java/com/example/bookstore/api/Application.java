package com.example.bookstore.api;

import com.example.bookstore.api.dtos.BookDTO;
import com.example.bookstore.api.entities.*;
import com.example.bookstore.api.repositories.*;
import com.example.bookstore.api.service.AuthorService;
import com.example.bookstore.api.service.BookService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Bookstore API", version = "1.0", description = "Bookstore API POC"))
public class Application {
	public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner(BookService bookService, AuthorService authorService) {
		return args -> {

			System.out.println("Preloading data...");
			LOGGER.info("adding William Shakespeare");
			Author williamShakespeare = authorService.saveAuthor(Author.builder()
					.name("William Shakespeare")
					.birthday(LocalDate.of(1564, 4, 23))
					.build());
			LOGGER.info("adding Agatha Christie");
			Author agathaChristie = authorService.saveAuthor(Author.builder()
					.name("Agatha Christie")
					.birthday(LocalDate.of(1890, 9, 12))
					.build());
			LOGGER.info("adding Roald Dahl");
			Author roaldDahl = authorService.saveAuthor(Author.builder()
					.name("Roald Dahl")
					.birthday(LocalDate.of(1913, 9, 13))
					.build());
			LOGGER.info("adding J. K. Rowling");
			Author jkRowling = authorService.saveAuthor(Author.builder()
					.name("J. K. Rowling")
					.birthday(LocalDate.of(1965, 7, 31))
					.build());

			LOGGER.info("adding Hamlet");
			bookService.saveBook(BookDTO.builder()
							.isbn("isbn1")
							.title("Hamlet")
							.price(10)
							.year(1603)
							.genre("Drama")
							.authors(Collections.singletonList(Author.builder().id(williamShakespeare.getId())
									.build()))
					.build());

			LOGGER.info("adding Murder on the Orient Express");
			bookService.saveBook(BookDTO.builder()
							.isbn("isbn2")
							.title("Murder on the Orient Express")
							.price(11)
							.year(1934)
							.genre("Mystery")
							.authors(Collections.singletonList(Author.builder().id(agathaChristie.getId())
									.build()))
					.build());

			LOGGER.info("adding Charlie and the Chocolate Factory");
			bookService.saveBook(BookDTO.builder()
							.isbn("ISBN 9783125737600")
							.title("Charlie and the Chocolate Factory")
							.price(12)
							.year(1967)
							.genre("Children")
							.authors(Collections.singletonList(Author.builder().id(roaldDahl.getId())
									.build()))
					.build());

			LOGGER.info("adding Harry Potter and the Philosopher's Stone");
			bookService.saveBook(BookDTO.builder()
							.isbn("ISBN 978-0-7475-3269-9")
							.title("Harry Potter and the Philosopher's Stone")
							.price(13)
							.year(1997)
							.genre("Fantasy")
							.authors(Collections.singletonList(Author.builder().id(jkRowling.getId())
									.build()))
					.build());

			LOGGER.info("adding Joint book authors");
			bookService.saveBook(BookDTO.builder()
							.isbn("ISBN5")
							.title("Joint book authors")
							.price(99)
							.year(2024)
							.genre("Fiction")
							.authors(List.of(
									Author.builder().id(williamShakespeare.getId())
											.build(),
									Author.builder().id(agathaChristie.getId())
											.build(),
									Author.builder().id(roaldDahl.getId())
											.build(),
									Author.builder().id(jkRowling.getId())
											.build()))
					.build());

			System.out.println("Data loaded.");
		};
	}
}
