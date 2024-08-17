package com.guipadovan.librarymanager.controllers;

import com.guipadovan.librarymanager.dtos.BookDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.GoogleBooksService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing books.
 * Provides endpoints for creating, retrieving, updating, and deleting books.
 */
@RestController
@RequestMapping("/v1/books")
public class BookController {

    private final BookService bookService;
    private final GoogleBooksService googleBooksService;

    public BookController(BookService bookService, GoogleBooksService googleBooksService) {
        this.bookService = bookService;
        this.googleBooksService = googleBooksService;
    }

    /**
     * Creates a new book.
     *
     * @param bookDetails the details of the book to create
     *
     * @return the created book
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDto bookDetails) {
        Book createdBook = bookService.createBook(bookDetails);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     *
     * @return the book with the given ID, if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBook(id);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(Book.class, id.toString());
        }
    }

    /**
     * Retrieves a paginated list of books.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of books per page (default is 10)
     *
     * @return a paginated list of books
     */
    @GetMapping
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Book> booksPage = bookService.getAllBooks(page, size);
        return new ResponseEntity<>(booksPage, HttpStatus.OK);
    }

    /**
     * Updates the details of an existing book.
     *
     * @param id          the ID of the book to update
     * @param bookDetails the new details for the book
     *
     * @return the updated book
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     *
     * @return a response entity with no content if the book was deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new EntityNotFoundException(Book.class, id.toString());
        }
    }

    /**
     * Searches for books by title using the Google Books API.
     *
     * @param title the title of the book to search for
     *
     * @return a list of books that match the title
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam("title") String title) {
        try {
            List<Book> books = googleBooksService.searchBooksByTitle(title);
            return ResponseEntity.ok(books);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
