package com.unir.inventory_books.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.inventory_books.controller.model.BookDto;
import com.unir.inventory_books.controller.model.CreateBookRequest;
import com.unir.inventory_books.data.BookRepository;
import com.unir.inventory_books.data.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BookRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public List<Book> getBooks(String title, String author, String publicationDate, String category, String isbn, Integer rating, Boolean visible) {
        log.info("Paso 2");

        if (StringUtils.hasLength(title) || StringUtils.hasLength(author) || StringUtils.hasLength(publicationDate)
                || StringUtils.hasLength(category) || StringUtils.hasLength(isbn) || rating != null || visible != null) {
            return repository.search(title, author, publicationDate, category, isbn, rating, visible);
        }

        List<Book> books = repository.getBooks();
        return books.isEmpty() ? null : books;
    }

    @Override
    public Book getBook(String bookId) {
        return repository.getById(Long.valueOf(bookId));
    }

    @Override
    public Boolean removeBook(String bookId) {
        Book book = repository.getById(Long.valueOf(bookId));

        if (book != null) {
            repository.delete(book);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Book createBook(CreateBookRequest request) {
        if (request != null && StringUtils.hasLength(request.getTitle().trim())
                && StringUtils.hasLength(request.getAuthor().trim())
                && StringUtils.hasLength(request.getPublicationDate().trim())
                && StringUtils.hasLength(request.getCategory().trim())
                && StringUtils.hasLength(request.getIsbn().trim())
                && request.getRating() != null && request.getVisible() != null) {

            Book book = Book.builder()
                    .title(request.getTitle())
                    .author(request.getAuthor())
                    .publicationDate(request.getPublicationDate())
                    .category(request.getCategory())
                    .isbn(request.getIsbn())
                    .rating(request.getRating())
                    .visible(request.getVisible())
                    .build();

            return repository.save(book);
        } else {
            return null;
        }
    }

    @Override
    public Book updateBook(String bookId, String request) {
        Book book = repository.getById(Long.valueOf(bookId));
        if (book != null) {
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
                Book patched = objectMapper.treeToValue(target, Book.class);
                repository.save(patched);
                return patched;
            } catch (JsonProcessingException | JsonPatchException e) {
                log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Book updateBook(String bookId, BookDto updateRequest) {
        Book book = repository.getById(Long.valueOf(bookId));
        if (book != null) {
            book.update(updateRequest);
            repository.save(book);
            return book;
        } else {
            return null;
        }
    }
}