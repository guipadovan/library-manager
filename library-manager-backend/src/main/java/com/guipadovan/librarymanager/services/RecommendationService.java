package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.entities.Book;

import java.util.List;

public interface RecommendationService {

    /**
     * Retrieves a limited list of recommended books for a user.
     * The recommendations are based on the user's reading history and the categories of the books.
     *
     * @param userId the ID of the user to get recommendations for
     * @param limit  the maximum number of recommendations to return
     *
     * @return a list of Book objects
     */
    List<Book> getBookRecommendationsByUser(Long userId, int limit);
}
