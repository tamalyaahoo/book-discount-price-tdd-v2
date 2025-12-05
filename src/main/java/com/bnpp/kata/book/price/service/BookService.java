package com.bnpp.kata.book.price.service;

import com.bnpp.kata.book.price.dto.Book;
import com.bnpp.kata.book.price.dto.BookPriceResponse;
import com.bnpp.kata.book.price.dto.BookResponse;
import com.bnpp.kata.book.price.exception.InvalidBookException;
import com.bnpp.kata.book.price.mapper.BookMapper;
import com.bnpp.kata.book.price.store.BookEnum;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        this.validateBasket(bookList);
        Map<String, Integer> mergedBookQuantity  = mergeDuplicateTitles(bookList);
        List<Integer> sortedCounts = extractSortedCounts(mergedBookQuantity);
        if (sortedCounts.isEmpty()) {
            throw new InvalidBookException("Basket must contain at least one book with quantity > 0");
        }
        double optimalPrice = computeOptimalPrice(sortedCounts, new HashMap<>());

        return new BookPriceResponse(optimalPrice);
    }

    private void validateBasket(List<Book> items) {
        requireNonNullList(items);
        requireNonEmptyList(items);
        validateEachBookItem(items);
        ensureAtLeastOnePositiveQuantity(items);
    }

    private void requireNonNullList(List<Book> items) {
        Optional.ofNullable(items)
                .orElseThrow(() -> new InvalidBookException("Basket must not be null"));
    }

    private void requireNonEmptyList(List<Book> items) {
        Optional.of(items)
                .filter(bookList -> !bookList.isEmpty())
                .orElseThrow(() -> new InvalidBookException("Basket must contain at least one entry"));
    }

    private void validateEachBookItem(List<Book> items) {
        items.forEach(item -> {
            String title = Optional.ofNullable(item.title())
                    .map(String::trim)
                    .filter(bookTitle -> !bookTitle.isEmpty())
                    .orElseThrow(() ->
                            new InvalidBookException("Book title must not be null or empty"));

            Integer qty = Optional.of(item.quantity())
                    .orElseThrow(() ->
                            new InvalidBookException("Quantity for book '%s' must not be null".formatted(title)));

            Optional.of(qty)
                    .filter(quantity -> quantity >= 0)
                    .orElseThrow(() ->
                            new InvalidBookException("Quantity for book '%s' must not be negative".formatted(title)));
        });
    }

    private void ensureAtLeastOnePositiveQuantity(List<Book> items) {
        items.stream()
                .map(Book::quantity)
                .filter(Objects::nonNull)
                .filter( count-> count > 0)
                .findFirst()
                .orElseThrow(() ->
                        new InvalidBookException("Basket must contain at least one book with quantity > 0"));
    }

    private Map<String, Integer> mergeDuplicateTitles(List<Book> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        item -> normalizeTitle(item.title()),
                        Book::quantity,
                        Integer::sum
                ));
    }

    private String normalizeTitle(String title) {
        return title.trim().toLowerCase();
    }

    private List<Integer> extractSortedCounts(Map<String, Integer> merged) {
        return merged.values().stream()
                .filter(quantity -> quantity != null && quantity > 0)
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    private double computeOptimalPrice(List<Integer> bookCounts, Map<String, Double> cache) {
        List<Integer> normalized = normalizeCounts(bookCounts);
        if (normalized.isEmpty()) {
            return 0.0;
        }
        String key = normalized.toString();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        double bestPrice = tryAllGroupSizes(normalized, cache);
        cache.put(key, bestPrice);
        return bestPrice;
    }

    private List<Integer> normalizeCounts(List<Integer> counts) {
        return counts.stream()
                .filter(count -> count > 0)
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    private double tryAllGroupSizes(List<Integer> state, Map<String, Double> cache) {
        int maxGroupSize = state.size();
        return IntStream.rangeClosed(1, maxGroupSize)
                .mapToDouble(size -> computeCostForGroup(size, state, cache))
                .min()
                .orElse(Double.MAX_VALUE);
    }

    private double computeCostForGroup(int groupSize, List<Integer> state, Map<String, Double> cache) {
        List<Integer> newState = applyGroupSelection(state, groupSize);
        double discount = DISCOUNTS.getOrDefault(groupSize, 0.0);
        double groupCost = groupSize * BOOK_PRICE * (1 - discount);
        double recursiveCost = computeOptimalPrice(newState, cache);
        return groupCost + recursiveCost;
    }

    private List<Integer> applyGroupSelection(List<Integer> state, int groupSize) {
        return IntStream.range(0, state.size())
                .map(i -> i < groupSize ? state.get(i) - 1 : state.get(i))
                .boxed()
                .toList();
    }
}
