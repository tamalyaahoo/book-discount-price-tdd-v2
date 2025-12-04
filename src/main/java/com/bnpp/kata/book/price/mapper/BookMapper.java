package com.bnpp.kata.book.price.mapper;

import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.store.BookEnum;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponse toResponse(BookEnum book){
        return new BookResponse(
                book.id(),
                book.title(),
                book.author(),
                book.year(),
                book.price()
        );
    }
}
