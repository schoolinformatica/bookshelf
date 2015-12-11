package com.example.bookshelf.dependencies;

import com.example.bookshelf.model.Book;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Wesley de Roode on 20-6-2015.
 */
public class ListViewSorter {

    /* Comparators for ArrayList sorting purposes */
    private static Comparator<Book> titleComparatorAscending = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            String bookTitle1 = lhs.getTitle().toUpperCase();
            String bookTitle2 = rhs.getTitle().toUpperCase();

            // Ascending order
            return bookTitle1.compareTo(bookTitle2);
        }
    };

    private static Comparator<Book> titleComparatorDescending = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            String bookTitle1 = lhs.getTitle().toUpperCase();
            String bookTitle2 = rhs.getTitle().toUpperCase();

            // Descending order
            return bookTitle2.compareTo(bookTitle1);
        }
    };

    private static Comparator<Book> authorComparatorAscending = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            String bookAuthor1 = lhs.getAuthor().toUpperCase();
            String bookAuthor2 = rhs.getAuthor().toUpperCase();

            // Descending order
            return bookAuthor1.compareTo(bookAuthor2);
        }
    };

    private static Comparator<Book> authorComparatorDescending = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            String bookAuthor1 = lhs.getAuthor().toUpperCase();
            String bookAuthor2 = rhs.getAuthor().toUpperCase();

            // Descending order
            return bookAuthor2.compareTo(bookAuthor1);
        }
    };

    public static List<Book> sortBookByTitle(List<Book> toBesSorted, String option) {
        List<Book> sorted = toBesSorted;

        if (option.equals("asc")) {
            Collections.sort(sorted, titleComparatorAscending);
        } else if (option.equals("desc")) {
            Collections.sort(sorted, titleComparatorDescending);
        }

        return sorted;
    }

    public static List<Book> sortBookByAuthor(List<Book> toBesSorted, String option) {
        List<Book> sorted = toBesSorted;

        if (option.equals("asc")) {
            Collections.sort(sorted, authorComparatorAscending);
        } else if (option.equals("desc")) {
            Collections.sort(sorted, authorComparatorDescending);
        }

        return sorted;
    }
}
