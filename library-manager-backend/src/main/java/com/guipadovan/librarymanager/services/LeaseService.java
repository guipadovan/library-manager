package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.dtos.LeaseDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.Lease;

import java.util.List;

/**
 * Service interface for managing book leases.
 */
public interface LeaseService {

    /**
     * Creates a new book lease.
     *
     * @param leaseDetails the lease DTO containing the lease details
     *
     * @return the created Lease entity
     */
    Lease createLease(LeaseDto leaseDetails);

    /**
     * Updates an existing book lease.
     *
     * @param id the ID of the book to be returned
     *
     * @return the updated Lease entity
     */
    Lease returnBook(Long id);

    /**
     * Retrieves a list of books that a user has leased.
     *
     * @param userId the ID of the user to get leased book IDs for
     *
     * @return a list of Book objects containing the leased books
     */
    List<Book> getLeasedBooksByUser(Long userId);
}
