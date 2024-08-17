package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.dtos.BookDto;
import com.guipadovan.librarymanager.entities.Book;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Service interface for managing books.
 */
public interface BookService {

    /**
     * Creates a new book.
     *
     * @param bookDetails the book DTO containing the book details
     *
     * @return the created Book entity
     */
    Book createBook(BookDto bookDetails);

    /**
     * Updates an existing book.
     *
     * @param bookDetails the book DTO containing the updated book details
     *
     * @return the updated Book entity
     */
    Book updateBook(BookDto bookDetails);

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     *
     * @return an Optional containing the book if found, or an empty Optional if not found
     */
    Optional<Book> getBook(Long id);

    /**
     * Retrieves a page of books.
     *
     * @param page the page number to retrieve (starting from 0)
     * @param size the number of books per page
     *
     * @return a page of Book objects
     */
    Page<Book> getAllBooks(int page, int size);

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     *
     * @return true if the book was successfully deleted, false otherwise
     */
    boolean deleteBook(Long id);
}
