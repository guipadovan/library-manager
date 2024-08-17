package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.dtos.LoanDto;
import com.guipadovan.librarymanager.entities.Loan;
import com.guipadovan.librarymanager.exceptions.InputValidationException;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Service interface for managing loans.
 */
public interface LoanService {

    /**
     * Creates a new loan.
     *
     * @param loanDetails the loan DTO containing the loan details
     */
    void createLoan(LoanDto loanDetails);

    /**
     * Updates an existing loan.
     *
     * @param id   the ID of the loan to update
     * @param loanDetails the loan DTO containing the updated loan details
     */
    void updateLoan(Long id, LoanDto loanDetails);

    /**
     * Retrieves a loan by its ID.
     *
     * @param id the ID of the loan to retrieve
     *
     * @return an Optional containing the loan if found, or an empty Optional if not found
     */
    Optional<Loan> getLoan(Long id);

    /**
     * Retrieves a page of loans.
     *
     * @param page the page number to retrieve (starting from 0)
     * @param size the number of loans per page
     *
     * @return a page of Loan objects
     */
    Page<Loan> getAllLoans(int page, int size);

    /**
     * Deletes a loan by its ID.
     *
     * @param id the ID of the loan to delete
     */
    void deleteLoan(Long id);
}
