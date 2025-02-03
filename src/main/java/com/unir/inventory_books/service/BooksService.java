package com.unir.inventory_books.service;

import com.unir.inventory_books.data.model.Book;
import com.unir.inventory_books.controller.model.BookDto;
import com.unir.inventory_books.controller.model.CreateBookRequest;

import java.util.List;

public interface BooksService {
    List<Book> getBooks(String title, String author, String publicationDate, String category, String isbn, Integer rating, Boolean visible);
    Book getBook(String bookId);
    Boolean removeBook(String bookId);
    Book createBook(CreateBookRequest request);
    Book updateBook(String bookId, String updateRequest);
    Book updateBook(String bookId, BookDto updateRequest);
}
