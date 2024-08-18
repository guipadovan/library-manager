package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.dtos.BookDto;
import com.guipadovan.librarymanager.entities.Book;
import org.springframework.data.domain.Page;

import java.util.List;
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
     * @param id          the ID of the book to update
     * @param bookDetails the book DTO containing the updated book details
     *
     * @return the updated Book entity
     */
    Book updateBook(Long id, BookDto bookDetails);

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
     * Retrieves a limited list of recommended books based on the provided categories and excluded book IDs.
     * It only returns books that are available in the library.
     *
     * @param categories      the list of categories to filter the books by
     * @param excludedBookIds the list of book IDs to exclude from the recommendations
     * @param limit           the maximum number of recommendations to return
     *
     * @return a list of Book objects
     */
    List<Book> getBookRecommendations(List<String> categories, List<Long> excludedBookIds, int limit);

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     *
     * @return true if the book was successfully deleted, false otherwise
     */
    boolean deleteBook(Long id);
}
