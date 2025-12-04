package com.bnpp.kata.book.price.service;

import com.bnpp.kata.book.price.dto.Book;
import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookServiceTest {

    private BookService service;

    @BeforeEach
    void setup() {
        service = new BookService(new BookMapper()); // FIX: inject mapper
    }

    @Test
    @DisplayName("getAllBooks() → returns exactly 5 books from enum")
    void testGetAllBooksCount() {
        List<BookResponse> books;
        books = service.getAllBooks();
        assertEquals(5, books.size());
    }

    //============================================================================

    @Test
    @DisplayName("Calculate price for a single book with no discount")
    void testSingleBook_noDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1)
        );
        assertEquals(50.0, service.calculatePrice(items), 0.01);
    }

    @Test
    @DisplayName("Apply 5% discount for two different books")
    void testTwoDifferentBooks_5PercentDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1),
                new Book("The Clean Coder", 1)
        );
        assertEquals(95.0, service.calculatePrice(items), 0.01);
    }

    @Test
    @DisplayName("Apply 10% discount for three different books")
    void testThreeDifferentBooks_10PercentDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1),
                new Book("The Clean Coder", 1),
                new Book("Clean Architecture", 1)
        );
        assertEquals(135.0, service.calculatePrice(items), 0.01);
    }

    @Test
    @DisplayName("Apply 20% discount for four different books")
    void testFourDifferentBooks_20PercentDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1),
                new Book("The Clean Coder", 1),
                new Book("Clean Architecture", 1),
                new Book("TDD", 1)
        );

        assertEquals(160.0, service.calculatePrice(items), 0.01);
    }
}