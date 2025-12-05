package com.bnpp.kata.book.price.service;

import com.bnpp.kata.book.price.dto.Book;
import com.bnpp.kata.book.price.dto.BookPriceResponse;
import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.mapper.BookMapper;
import com.bnpp.kata.book.price.store.BookEnum;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class BookService {

    private static final double BOOK_PRICE = 50.0;

    private static final Map<Integer, Double> DISCOUNTS = Map.of(
            1, 0.00,
            2, 0.05,
            3, 0.10,
            4, 0.20,
            5, 0.25
    );

    private final BookMapper mapper;

    public BookService(BookMapper mapper) {
        this.mapper = mapper;
    }

    public List<BookResponse> getAllBooks() {
        return Stream.of(BookEnum.values())
                .map(mapper :: toResponse)
                .toList();
    }

    public BookPriceResponse calculatePrice(List<Book> bookList) {
        int reqBookCount = bookList.size();
        double discount = DISCOUNTS.getOrDefault(reqBookCount, 0.0);
        double totalPrice;
        totalPrice = (BOOK_PRICE * reqBookCount) * (1 - discount);
        return new BookPriceResponse(totalPrice);
    }
}
