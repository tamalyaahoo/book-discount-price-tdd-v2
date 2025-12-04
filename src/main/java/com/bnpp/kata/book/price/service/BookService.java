package com.bnpp.kata.book.price.service;

import com.bnpp.kata.book.price.dto.Book;
import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.mapper.BookMapper;
import com.bnpp.kata.book.price.store.BookEnum;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Stream;

@Service
public class BookService {

    private static final double BOOK_PRICE = 50.0;

    private final BookMapper mapper;

    public BookService(BookMapper mapper) {
        this.mapper = mapper;
    }

    public List<BookResponse> getAllBooks() {
        return Stream.of(BookEnum.values())
                .map(mapper :: toResponse)
                .toList();
    }

    public double calculatePrice(List<Book> bookList) {
        int reqBookCount = bookList.size();
        double totalPrice = 0.0;
        if(reqBookCount == 1)
            totalPrice = BOOK_PRICE * reqBookCount;
        else if(reqBookCount == 2){
            totalPrice = (BOOK_PRICE * reqBookCount) * (1 - 0.05);
        }else if(reqBookCount == 3){
            totalPrice = (BOOK_PRICE * reqBookCount) * (1 - 0.10);
    }
        return totalPrice;
    }
}
