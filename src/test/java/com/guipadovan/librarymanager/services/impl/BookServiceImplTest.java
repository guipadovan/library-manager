package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.BookDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void createBook_ShouldReturnCreatedBook_WhenInputIsValid() {
        BookDto bookDto = new BookDto("Título de Teste", "Autor de Teste", "1234567890123", LocalDate.now(), "Ficção");
        Book book = new Book(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getIsbn(), bookDto.getPublicationDate(), bookDto.getCategory());

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.createBook(bookDto);

        assertNotNull(createdBook);
        assertEquals(bookDto.getTitle(), createdBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook_WhenIdExists() {
        Long bookId = 1L;
        BookDto bookDto = new BookDto("Título Atualizado", "Autor Atualizado", "9876543210987", LocalDate.now(), "Não-ficção");
        Book existingBook = new Book("Título Antigo", "Autor Antigo", "1111111111111", LocalDate.now().minusDays(1), "Ficção");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Book updatedBook = bookService.updateBook(bookId, bookDto);

        assertNotNull(updatedBook);
        assertEquals(bookDto.getTitle(), updatedBook.getTitle());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void updateBook_ShouldThrowEntityNotFoundException_WhenIdDoesNotExist() {
        Long nonExistentId = 1L;
        BookDto bookDto = new BookDto("Test Title", "Test Author", "123456789", LocalDate.now(), "Test Category");

        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(nonExistentId, bookDto));
    }

    @Test
    void getBook_ShouldReturnBook_WhenIdExists() {
        Long bookId = 1L;
        Book book = new Book("Título de Teste", "Autor de Teste", "1234567890123", LocalDate.now(), "Ficção");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBook(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void getAllBooks_ShouldReturnPageOfBooks() {
        int page = 0;
        int size = 5;
        List<Book> books = Arrays.asList(new Book("Título1", "Autor1", "1111111111111", LocalDate.now(), "Categoria1"),
                new Book("Título2", "Autor2", "2222222222222", LocalDate.now(), "Categoria2"));
        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAll(PageRequest.of(page, size))).thenReturn(bookPage);

        Page<Book> result = bookService.getAllBooks(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(bookRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    void getAllBooks_ShouldReturnEmptyPage_WhenNoBooksExist() {
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(Page.empty());

        Page<Book> result = bookService.getAllBooks(0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void getBookRecommendations_ShouldReturnRecommendations() {
        List<String> categories = Arrays.asList("Ficção", "Ciência");
        List<Long> excludedIds = Arrays.asList(1L, 2L);
        int limit = 2;
        List<Book> recommendedBooks = Arrays.asList(new Book("Título1", "Autor1", "1111111111111", LocalDate.now(), "Ficção"),
                new Book("Título2", "Autor2", "2222222222222", LocalDate.now(), "Ciência"));

        when(bookRepository.findBooksByCategoriesRandomizedAndOrdered(categories, excludedIds, PageRequest.of(0, limit)))
                .thenReturn(recommendedBooks);

        List<Book> result = bookService.getBookRecommendations(categories, excludedIds, limit);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findBooksByCategoriesRandomizedAndOrdered(categories, excludedIds, PageRequest.of(0, limit));
    }

    @Test
    void getBookRecommendations_ShouldHandleEmptyCategories() {
        List<Long> excludedBookIds = Arrays.asList(1L, 2L);

        when(bookRepository.findBooksByCategoriesRandomizedAndOrdered(any(List.class), eq(excludedBookIds), any(PageRequest.class)))
                .thenReturn(List.of());

        List<Book> result = bookService.getBookRecommendations(List.of(), excludedBookIds, 5);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteBook_ShouldReturnTrue_WhenIdExists() {
        Long bookId = 1L;

        when(bookRepository.deleteByIdInt(bookId)).thenReturn(1);

        boolean result = bookService.deleteBook(bookId);

        assertTrue(result);
        verify(bookRepository, times(1)).deleteByIdInt(bookId);
    }

    @Test
    void deleteBook_ShouldReturnFalse_WhenIdDoesNotExist() {
        Long bookId = 1L;

        when(bookRepository.deleteByIdInt(bookId)).thenReturn(0);

        boolean result = bookService.deleteBook(bookId);

        assertFalse(result);
        verify(bookRepository, times(1)).deleteByIdInt(bookId);
    }
}
