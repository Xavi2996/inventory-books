package com.unir.inventory_books.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.unir.inventory_books.controller.model.BookDto;
import com.unir.inventory_books.controller.responses.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.inventory_books.data.model.Book;
import com.unir.inventory_books.controller.model.CreateBookRequest;
import com.unir.inventory_books.service.BooksService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BooksController {

    private final BooksService service;

    @GetMapping("/books")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    public ResponseEntity<BookResponse> getBooks(
            @RequestHeader Map<String, String> headers,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publicationDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible) {

        log.info("Paso 1");
        log.info("headers: {}", headers);
        List<Book> books = service.getBooks(title, author, publicationDate, category, isbn, rating, visible);
        log.info("books: {}", books);

        if (rating != null && (rating < 0 || rating > 5)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookResponse(null, "El rating debe estar entre 0 y 5"));
        }

        if (books != null && !books.isEmpty()) {
            return ResponseEntity.ok(new BookResponse(books, "Libros encontrados"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BookResponse(null, "No Se encontraron Libros"));
        }
    }

    @GetMapping("/books/{bookId}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<BookResponse> getBook(@PathVariable String bookId) {

        log.info("Request received for book {}", bookId);
        Book book = service.getBook(bookId);

        if (book != null) {
            return ResponseEntity.ok(new BookResponse(Collections.singletonList(book), "Libro encontrado"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BookResponse(null, "No se ha encontrado el libro con el identificador indicado."));
        }

    }

    @DeleteMapping("/books/{bookId}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<BookResponse> deleteBook(@PathVariable String bookId) {

        Boolean removed = service.removeBook(bookId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok(new BookResponse(null, "Libro eliminado exitosamente"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BookResponse(null, "No se ha encontrado el libro con el identificador indicado"));
        }

    }

    @PostMapping("/books")
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<BookResponse> addBook(@RequestBody CreateBookRequest request) {

        Book createdBook = service.createBook(request);

        if (createdBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BookResponse(Collections.singletonList(createdBook), "Libro creado"));
        } else {
            return ResponseEntity.badRequest().body(new BookResponse(null, "Datos introducidos incorrectos"));
        }
    }


    @PatchMapping("/books/{bookId}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro inválido o datos incorrectos introducidos.")
    public ResponseEntity<BookResponse> patchBook(@PathVariable String bookId, @RequestBody String patchBody) {

        Book patched = service.updateBook(bookId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(new BookResponse(Collections.singletonList(patched), "Libro actualizado"));
        } else {
            return ResponseEntity.badRequest().body(new BookResponse(null, "Libro inválido o datos incorrectos introducidos"));
        }
    }


    @PutMapping("/books/{bookId}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro no encontrado.")
    public ResponseEntity<BookResponse> updateBook(@PathVariable String bookId, @RequestBody BookDto body) {

        Book updated = service.updateBook(bookId, body);
        if (updated != null) {
            return ResponseEntity.ok(new BookResponse(Collections.singletonList(updated), "Libro actualizado"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}