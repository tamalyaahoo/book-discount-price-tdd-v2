package com.bnpp.kata.book.price.store;


public enum BookEnum implements BookInfo {
    CLEAN_CODE(1, "Clean Code", Author.RM, 2008, 50.0),
    THE_CLEAN_CODER(2, "The Clean Coder", Author.RM, 2011, 50.0),
    CLEAN_ARCHITECTURE(3, "Clean Architecture", Author.RM, 2017, 50.0),
    TDD_BY_EXAMPLE(4, "Test Driven Development by Example", Author.KB, 2003, 50.0),
    LEGACY_CODE(5, "Working Effectively with Legacy Code", Author.MF, 2004, 50.0);

    private final BookInfo info;

    BookEnum(int id, String title, Author author, int year, double price) {
        this.info = new BookInfoRecord(id, title, author.getFullName(), year, price);
    }

    @Override
    public int id() { return info.id(); }

    @Override
    public String title() { return info.title(); }

    @Override
    public String author() { return info.author(); }

    @Override
    public int year() { return info.year(); }

    @Override
    public double price() { return info.price(); }

    // ------------------------------
    //   Record representing Book data
    // ------------------------------
    public record BookInfoRecord(int id,
                                 String title,
                                 String author,
                                 int year,
                                 double price
    ) implements BookInfo {}

}
