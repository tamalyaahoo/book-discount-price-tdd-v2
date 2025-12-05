package com.bnpp.kata.book.price.store;


public enum Author {
    RM("Robert Martin"),
    KB("Kent Beck"),
    MF("Michael Feathers");


    private final String fullName;

    Author(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
