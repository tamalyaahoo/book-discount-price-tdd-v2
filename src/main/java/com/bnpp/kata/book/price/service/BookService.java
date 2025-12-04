package com.bnpp.kata.book.price.service;

import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.store.BookEnum;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Stream;

@Service
public class BookService {

    public List<BookResponse> getAllBooks() {
        return Stream.of(BookEnum.values())
                .map(book -> new BookResponse(book.id(), book.title(), book.author(), book.year(), book.price()))
                .toList();
    }
}
