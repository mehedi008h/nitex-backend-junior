package com.mehedi.nitex.service;

import com.mehedi.nitex.exceptions.model.NotFoundException;
import com.mehedi.nitex.model.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    List<Book> getBooks();
    Book findBookById(Long id);
    Book checkBookExist(Long id) throws NotFoundException;
    void deleteBook(Long id) throws NotFoundException;
    Book bookDetails(Long id) throws NotFoundException;
    Book updateBook(
            Book Book
    ) throws NotFoundException;
}
