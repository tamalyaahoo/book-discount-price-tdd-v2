package com.bnpp.kata.book.price.store;

import lombok.Getter;

public enum Author {
    RM("Robert Martin"),
    KB("Kent Beck"),
    MF("Michael Feathers");

    @Getter
    private final String fullName;

    Author(String fullName) {
        this.fullName = fullName;
    }

}
