package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.dtos.LeaseDto;
import com.guipadovan.librarymanager.entities.Lease;
import org.springframework.data.domain.Page;

import java.util.Optional;

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
     * @param id          the ID of the lease to update
     * @param leaseDetails the lease DTO containing the updated lease details
     *
     * @return the updated Lease entity
     */
    Lease updateLease(Long id, LeaseDto leaseDetails);

    /**
     * Retrieves a book lease by its ID.
     *
     * @param id the ID of the lease to retrieve
     *
     * @return an Optional containing the lease if found, or an empty Optional if not found
     */
    Optional<Lease> getLease(Long id);

    /**
     * Retrieves a page of book leases.
     *
     * @param page the page number to retrieve (starting from 0)
     * @param size the number of leases per page
     *
     * @return a page of Lease objects
     */
    Page<Lease> getAllLeases(int page, int size);

    /**
     * Deletes a book lease by its ID.
     *
     * @param id the ID of the lease to delete
     *
     * @return true if the lease was successfully deleted, false otherwise
     */
    boolean deleteLease(Long id);
}
