package com.bnpp.kata.book.price.service;

import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.mapper.BookMapper;
import com.bnpp.kata.book.price.store.BookEnum;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Stream;

@Service
public class BookService {

    private final BookMapper mapper;

    public BookService(BookMapper mapper) {
        this.mapper = mapper;
    }

    public List<BookResponse> getAllBooks() {
        return Stream.of(BookEnum.values())
                .map(mapper :: toResponse)
                .toList();
    }
}
