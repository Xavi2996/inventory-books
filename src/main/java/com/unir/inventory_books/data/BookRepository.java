package com.unir.inventory_books.data;
import com.unir.inventory_books.data.utils.Consts;
import com.unir.inventory_books.data.utils.SearchCriteria;
import com.unir.inventory_books.data.utils.SearchOperation;
import com.unir.inventory_books.data.utils.SearchStatement;
import com.unir.inventory_books.data.model.Book;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final BookJpaRepository repository;

    public List<Book> getBooks()
    {     System.out.println("Paso 3");

        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }
    public List<Book> search(String title, String author, String publicationDate, String category, String isbn, Integer rating, Boolean visible) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(title)) {
            spec.add(new SearchStatement(Consts.TITLE, title, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(author)) {
            spec.add(new SearchStatement(Consts.AUTHOR, author, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(publicationDate)) {
            spec.add(new SearchStatement(Consts.PUBLICATION_DATE, publicationDate, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(category)) {
            spec.add(new SearchStatement(Consts.CATEGORY, category, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(isbn)) {
            spec.add(new SearchStatement(Consts.ISBN, isbn, SearchOperation.MATCH));
        }

        if (rating != null) {
            spec.add(new SearchStatement(Consts.RATING, rating, SearchOperation.EQUAL));
        }

        if (visible != null) {
            spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }
}
