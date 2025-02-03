package com.unir.inventory_books.data.model;

import com.unir.inventory_books.controller.model.BookDto;
import com.unir.inventory_books.data.utils.Consts;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publication_date")
    private String publicationDate;

    @Column(name = "category")
    private String category;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "visible")
    private Boolean visible;

    public void update(BookDto bookDto) {
        this.title = bookDto.getTitle();
        this.author = bookDto.getAuthor();
        this.publicationDate = bookDto.getPublicationDate();
        this.category = bookDto.getCategory();
        this.isbn = bookDto.getIsbn();
        this.rating = bookDto.getRating();
        this.visible = bookDto.getVisible();
    }
}
