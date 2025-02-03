package com.unir.inventory_books.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unir.inventory_books.data.model.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



interface BookJpaRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByVisible(Boolean visible);

    List<Book> findByTitleAndAuthor(String title, String author);

    List<Book> findByTitleContainingAndAuthorContainingAndPublicationDateContainingAndCategoryContainingAndIsbnContainingAndRatingAndVisible(
            String title, String author, String publicationDate, String category, String isbn, Integer rating, Boolean visible);
}
