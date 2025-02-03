package com.unir.inventory_books.controller.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
    private String title;
    private String author;
    private String publicationDate;
    private String category;
    private String isbn;
    private Integer rating;
    private Boolean visible;
}
