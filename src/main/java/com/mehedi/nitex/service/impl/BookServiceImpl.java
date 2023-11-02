package com.mehedi.nitex.service.impl;

import com.mehedi.nitex.exceptions.model.NotFoundException;
import com.mehedi.nitex.model.Book;
import com.mehedi.nitex.repository.BookRepository;
import com.mehedi.nitex.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findBooksById(id);
    }

    @Override
    public Book checkBookExist(Long id) throws NotFoundException {
        Book book = findBookById(id);
        if (book == null) throw new NotFoundException("Book not found");
        return book;
    }

    @Override
    public void deleteBook(Long id) throws NotFoundException {
        Book book = checkBookExist(id);
        bookRepository.deleteById(book.getId());
    }

    @Override
    public Book bookDetails(Long id) throws NotFoundException {
        Book book = checkBookExist(id);
        return book;
    }

    @Override
    public Book updateBook(Book book) throws NotFoundException {
       checkBookExist(book.getId());
        return bookRepository.save(book);
    }
}
