package com.bnpp.kata.book.price.mapper;

import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.store.BookEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    @Test
    @DisplayName("BookMapper should map each BookEnum constant into a valid BookResponse object")
    void testEnumToDtoMappingForAllBooks() {
        for (BookEnum book : BookEnum.values()) {
            BookResponse dto = mapper.toResponse(book);

            assertThat(dto)
                    .usingRecursiveComparison()
                    .isEqualTo(
                            new BookResponse(
                                    book.id(),
                                    book.title(),
                                    book.author(),
                                    book.year(),
                                    book.price()
                            )
                    );
        }
    }

}