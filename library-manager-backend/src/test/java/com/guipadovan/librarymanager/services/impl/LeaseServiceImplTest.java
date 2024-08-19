package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.LeaseDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.Lease;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.InputValidationException;
import com.guipadovan.librarymanager.repositories.LeaseRepository;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaseServiceImplTest {

    @Mock
    private LeaseRepository leaseRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private LeaseServiceImpl leaseService;

    @Test
    void createLease_ShouldCreateLease_WhenInputIsValid() throws InputValidationException {
        LeaseDto leaseDto = new LeaseDto();
        leaseDto.setUserId(1L);
        leaseDto.setBookId(1L);
        leaseDto.setLeaseDate(LocalDate.now());
        leaseDto.setReturnDate(LocalDate.now().plusDays(7));

        User user = new User();
        Book book = new Book();
        Lease expectedLease = new Lease();

        when(userService.getUser(any(Long.class))).thenReturn(Optional.of(user));
        when(bookService.getBook(any(Long.class))).thenReturn(Optional.of(book));
        when(leaseRepository.save(any(Lease.class))).thenReturn(expectedLease);

        Lease actualLease = leaseService.createLease(leaseDto);

        assertEquals(expectedLease, actualLease);
        verify(leaseRepository, times(1)).save(any(Lease.class));
    }

    @Test
    void createLease_ShouldThrowException_WhenUserNotFound() {
        LeaseDto leaseDto = new LeaseDto();
        leaseDto.setUserId(1L);

        when(userService.getUser(any(Long.class))).thenReturn(Optional.empty());

        InputValidationException exception = assertThrows(InputValidationException.class, () -> leaseService.createLease(leaseDto));

        assertTrue(exception.getFieldErrors().containsKey("userId"));
        verify(userService, times(1)).getUser(any(Long.class));
    }

    @Test
    void createLease_ShouldThrowException_WhenBookNotFound() {
        LeaseDto leaseDto = new LeaseDto();
        leaseDto.setBookId(1L);

        when(bookService.getBook(any(Long.class))).thenReturn(Optional.empty());

        InputValidationException exception = assertThrows(InputValidationException.class, () -> leaseService.createLease(leaseDto));

        assertTrue(exception.getFieldErrors().containsKey("bookId"));
        verify(bookService, times(1)).getBook(any(Long.class));
    }

    @Test
    void createLease_ShouldThrowException_WhenLeaseDetailsAreIncomplete() {
        LeaseDto leaseDto = new LeaseDto();

        InputValidationException exception = assertThrows(InputValidationException.class, () -> leaseService.createLease(leaseDto));

        assertFalse(exception.getFieldErrors().isEmpty());
    }

    @Test
    void returnBook_ShouldUpdateLeaseStatus_WhenInputIsValid() throws InputValidationException {
        Book book = new Book();
        Lease lease = new Lease();
        lease.setStatus(Lease.Status.ACTIVE);

        when(bookService.getBook(any(Long.class))).thenReturn(Optional.of(book));
        when(leaseRepository.existsByBook_IdAndStatusActive(any(Long.class))).thenReturn(true);
        when(leaseRepository.findByBook_IdAndStatusActive(any(Long.class))).thenReturn(lease);
        when(leaseRepository.save(any(Lease.class))).thenReturn(lease);

        Lease updatedLease = leaseService.returnBook(1L);

        assertEquals(Lease.Status.RETURNED, updatedLease.getStatus());
        verify(leaseRepository, times(1)).save(lease);
    }

    @Test
    void returnBook_ShouldThrowException_WhenBookNotFound() {
        when(bookService.getBook(any(Long.class))).thenReturn(Optional.empty());

        InputValidationException exception = assertThrows(InputValidationException.class, () -> leaseService.returnBook(1L));

        assertTrue(exception.getFieldErrors().containsKey("bookId"));
        verify(bookService, times(1)).getBook(any(Long.class));
    }

    @Test
    void returnBook_ShouldThrowException_WhenBookNotLeased() {
        when(bookService.getBook(any(Long.class))).thenReturn(Optional.of(new Book()));
        when(leaseRepository.existsByBook_IdAndStatusActive(any(Long.class))).thenReturn(false);

        InputValidationException exception = assertThrows(InputValidationException.class, () -> leaseService.returnBook(1L));

        assertTrue(exception.getFieldErrors().containsKey("bookId"));
    }
}
