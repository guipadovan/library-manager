package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.entities.Book;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for Google books api.
 */
public interface GoogleBooksService {

    /**
     * Searches for books by title using the Google Books API.
     *
     * @param title the title of the book to search for
     *
     * @return a list of books matching the title
     *
     * @throws IOException if there is an error during the API call or response parsing
     */
    List<Book> searchBooksByTitle(String title) throws IOException;

}
