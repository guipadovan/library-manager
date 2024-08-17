package com.guipadovan.librarymanager.controllers;

import com.guipadovan.librarymanager.dtos.LeaseDto;
import com.guipadovan.librarymanager.entities.Lease;
import com.guipadovan.librarymanager.services.LeaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing leases.
 * Provides endpoints for creating, retrieving, and returning leased books.
 */
@RestController
@RequestMapping("/v1/leases")
public class LeaseController {

    private final LeaseService leaseService;

    public LeaseController(LeaseService leaseService) {
        this.leaseService = leaseService;
    }

    /**
     * Creates a new lease.
     *
     * @param leaseDetails the details of the lease to create
     *
     * @return the created lease
     */
    @PostMapping
    public ResponseEntity<Lease> createLease(@Valid @RequestBody LeaseDto leaseDetails) {
        Lease createdLease = leaseService.createLease(leaseDetails);
        return new ResponseEntity<>(createdLease, HttpStatus.CREATED);
    }

    /**
     * Returns a leased book by lease ID.
     *
     * @param bookId the ID of the book to return
     *
     * @return the returned lease
     */
    @PostMapping("/{bookId}/return")
    public ResponseEntity<Lease> returnBook(@PathVariable Long bookId) {
        Lease returnedLease = leaseService.returnBook(bookId);
        return new ResponseEntity<>(returnedLease, HttpStatus.OK);
    }
}
