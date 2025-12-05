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
        assertEquals(50.0, service.calculatePrice(items).totalPrice(), 0.01);
    }

    @Test
    @DisplayName("Apply 5% discount for two different books")
    void testTwoDifferentBooks_5PercentDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1),
                new Book("The Clean Coder", 1)
        );
        assertEquals(95.0, service.calculatePrice(items).totalPrice(), 0.01);
    }

    @Test
    @DisplayName("Apply 10% discount for three different books")
    void testThreeDifferentBooks_10PercentDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1),
                new Book("The Clean Coder", 1),
                new Book("Clean Architecture", 1)
        );
        assertEquals(135.0, service.calculatePrice(items).totalPrice(), 0.01);
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

        assertEquals(160.0, service.calculatePrice(items).totalPrice(), 0.01);
    }

    @Test
    @DisplayName("Apply 25% discount for all five different books")
    void testAllFiveBooks_25PercentDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 1),
                new Book("The Clean Coder", 1),
                new Book("Clean Architecture", 1),
                new Book("TDD", 1),
                new Book("Legacy Code", 1)
        );

        assertEquals(187.50, service.calculatePrice(items).totalPrice(), 0.01);
    }

    @Test
    @DisplayName("Merge duplicate book titles ignoring case and sum their quantities")
    void testCaseInsensitiveMerging() {
        List<Book> items = List.of(
                new Book("clean code", 1),
                new Book("Clean Code", 2),
                new Book("CLEAN CODE", 3)
        );
        // Total = 1 + 2 + 3 = 6 copies of 1 book
        double price = service.calculatePrice(items).totalPrice();

        assertEquals(6 * 50.0, price, 0.01); // No discount because only 1 distinct title
    }

    @Test
    @DisplayName("Calculate optimal pricing for multi-set scenario resulting in 320 EUR")
    void testKataExample_multipleSets_Optimal320() {
        List<Book> items = List.of(
                new Book("Clean Code", 2),
                new Book("The Clean Coder", 2),
                new Book("Clean Architecture", 2),
                new Book("TDD", 1),
                new Book("Legacy Code", 1)
        );

        assertEquals(320.0, service.calculatePrice(items).totalPrice(), 0.01);
    }

    @Test
    @DisplayName("Calculate price when multiple copies of a single title are purchased with no discount")
    void testSameTitleMultipleCopies_noDiscount() {
        List<Book> items = List.of(
                new Book("Clean Code", 3)
        );
        assertEquals(150.0, service.calculatePrice(items).totalPrice(), 0.01);
    }

    @Test
    @DisplayName("Choose optimal grouping when some titles have multiple copies")
    void testThreeTitles_multipleCopies_mixedOptimal() {
        List<Book> items = List.of(
                new Book("Clean Code", 2),
                new Book("Clean Architecture", 1),
                new Book("The Clean Coder", 2)
        );
        assertEquals(230.0, service.calculatePrice(items).totalPrice(), 0.01);
    }
}