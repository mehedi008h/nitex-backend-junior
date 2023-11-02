package com.mehedi.nitex.repository;

import com.mehedi.nitex.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBooksById(Long id);
}
