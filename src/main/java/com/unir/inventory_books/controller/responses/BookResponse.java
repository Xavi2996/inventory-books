package com.unir.inventory_books.controller.responses;

import com.unir.inventory_books.data.model.Book;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponse {
    private List<Book> books;
    private String message;
}