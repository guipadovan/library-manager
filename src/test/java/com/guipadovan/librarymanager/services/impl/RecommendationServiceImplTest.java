package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.LeaseService;
import com.guipadovan.librarymanager.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @Mock
    private LeaseService leaseService;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @Test
    void getBookRecommendationsByUser_ShouldReturnRecommendations_WhenUserExists() throws EntityNotFoundException {
        Long userId = 1L;
        int limit = 5;
        User user = new User();
        Book book1 = new Book();
        book1.setCategory("Ficção");
        Book book2 = new Book();
        book2.setCategory("Ficção");

        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        when(leaseService.getLeasedBooksByUser(userId)).thenReturn(List.of(book1));
        when(bookService.getBookRecommendations(any(), any(), eq(limit))).thenReturn(List.of(book2));

        List<Book> recommendations = recommendationService.getBookRecommendationsByUser(userId, limit);

        assertEquals(1, recommendations.size());
        assertEquals("Ficção", recommendations.getFirst().getCategory());
        verify(leaseService, times(1)).getLeasedBooksByUser(userId);
        verify(bookService, times(1)).getBookRecommendations(any(), any(), eq(limit));
    }

    @Test
    void getBookRecommendationsByUser_ShouldReturnEmptyList_WhenUserHasLeasedNoBooks() throws EntityNotFoundException {
        Long userId = 2L;
        int limit = 5;
        User user = new User();

        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        when(leaseService.getLeasedBooksByUser(userId)).thenReturn(List.of());
        when(bookService.getBookRecommendations(any(), any(), eq(limit))).thenReturn(List.of());

        List<Book> recommendations = recommendationService.getBookRecommendationsByUser(userId, limit);

        assertEquals(0, recommendations.size());
        verify(leaseService, times(1)).getLeasedBooksByUser(userId);
        verify(bookService, times(1)).getBookRecommendations(any(), any(), eq(limit));
    }

    @Test
    void getBookRecommendationsByUser_ShouldReturnEmptyList_WhenNoBooksMatchTheCategories() throws EntityNotFoundException {
        Long userId = 3L;
        int limit = 5;
        User user = new User();
        Book leasedBook = new Book();
        leasedBook.setCategory("Ficção");

        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        when(leaseService.getLeasedBooksByUser(userId)).thenReturn(List.of(leasedBook));
        when(bookService.getBookRecommendations(any(), any(), eq(limit))).thenReturn(List.of());

        List<Book> recommendations = recommendationService.getBookRecommendationsByUser(userId, limit);

        assertEquals(0, recommendations.size());
        verify(leaseService, times(1)).getLeasedBooksByUser(userId);
        verify(bookService, times(1)).getBookRecommendations(any(), any(), eq(limit));
    }

    @Test
    void getBookRecommendationsByUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        int limit = 5;

        when(userService.getUser(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            recommendationService.getBookRecommendationsByUser(userId, limit);
        });

        assertEquals("User with id 1 not found.", exception.getMessage());
        verify(userService, times(1)).getUser(userId);
        verifyNoMoreInteractions(leaseService);
        verifyNoMoreInteractions(bookService);
    }
}
