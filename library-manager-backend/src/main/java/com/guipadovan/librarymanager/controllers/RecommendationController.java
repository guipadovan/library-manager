package com.guipadovan.librarymanager.controllers;

import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.services.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing book recommendations.
 * Provides endpoints for retrieving book recommendations based on user activity.
 */
@RestController
@RequestMapping("/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Retrieves book recommendations for the given user.
     *
     * @param userId the ID of the user
     * @param limit  the maximum number of recommendations to fetch
     *
     * @return the list of recommended books
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Book>> getBookRecommendations(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "10") int limit) {
        List<Book> recommendedBooks = recommendationService.getBookRecommendationsByUser(userId, limit);
        return ResponseEntity.ok(recommendedBooks);
    }
}
