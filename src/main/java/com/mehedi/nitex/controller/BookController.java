package com.mehedi.nitex.controller;

import com.mehedi.nitex.exceptions.model.NotFoundException;
import com.mehedi.nitex.model.Book;
import com.mehedi.nitex.model.HttpResponse;
import com.mehedi.nitex.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequestMapping(value = "/api/v1/book")
@RestController
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // add book
    @PostMapping()
    public ResponseEntity<Book> addNewBook(@RequestBody Book book) {
        Book newBook = bookService.addBook(book);
        return new ResponseEntity<>(newBook, OK);
    }

    // get all book
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBook( ){
        List<Book> books = bookService.getBooks();
        return new ResponseEntity<>(books, OK);
    }

    // get book details
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook( @PathVariable Long id) throws NotFoundException {
        Book book = bookService.bookDetails(id);
        return new ResponseEntity<>(book, OK);
    }

    // delete book
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete')")
    public ResponseEntity<HttpResponse> deleteBook(@PathVariable Long id) throws NotFoundException {
        bookService.deleteBook(id);
        return response(OK, "Book Delete Successfully");
    }

    // update book
    @PutMapping
    public ResponseEntity<Book> updateBook(@RequestBody Book book) throws NotFoundException {
        Book updateBook = bookService.updateBook(book);
        return new ResponseEntity<>(updateBook, OK);
    }

    // custom response
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
