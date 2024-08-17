package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.LeaseDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.Lease;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.exceptions.InputValidationException;
import com.guipadovan.librarymanager.repositories.LeaseRepository;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.LeaseService;
import com.guipadovan.librarymanager.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * Implementation of {@link LeaseService} interface.
 */
@Service
@Slf4j
public class LeaseServiceImpl implements LeaseService {

    private final LeaseRepository leaseRepository;
    private final UserService userService;
    private final BookService bookService;

    public LeaseServiceImpl(LeaseRepository leaseRepository, UserService userService, BookService bookService) {
        this.leaseRepository = leaseRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InputValidationException if there are validation errors
     */
    @Override
    public Lease createLease(LeaseDto leaseDetails) throws InputValidationException {
        Lease lease = new Lease();

        // Valida e mapeia os detalhes do empréstimo para o objeto lease
        validateAndMapLease(leaseDetails, lease);

        log.info("Creating lease {}", lease);
        return leaseRepository.save(lease);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException  if the lease is not found
     * @throws InputValidationException if there are validation errors
     */
    @Override
    public Lease updateLease(Long id, LeaseDto leaseDetails) throws InputValidationException {
        // Busca o empréstimo pelo ID, lança exceção se não encontrado
        Lease lease = leaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Lease.class, id.toString()));

        // Valida e mapeia os detalhes do empréstimo para o objeto lease
        validateAndMapLease(leaseDetails, lease);

        log.info("Updating lease {}", lease);
        return leaseRepository.save(lease);
    }

    /**
     * Validates the lease details and maps them to the lease object.
     *
     * @param leaseDetails The lease details to be validated and mapped.
     * @param lease        The lease object to be updated with the lease details.
     *
     * @throws InputValidationException if there are any validation errors.
     */
    private void validateAndMapLease(LeaseDto leaseDetails, Lease lease) throws InputValidationException {
        HashMap<String, String> fieldErrors = new HashMap<>();

        // Valida o usuário
        Optional<User> user = userService.getUser(leaseDetails.getUserId());
        if (user.isEmpty())
            fieldErrors.put("userId", "Usuário não encontrado");

        // Valida o livro
        Optional<Book> book = bookService.getBook(leaseDetails.getBookId());
        if (book.isEmpty())
            fieldErrors.put("bookId", "Livro não encontrado");
        else if (leaseRepository.existsByBook_IdAndStatusActive(book.get().getId()))
            fieldErrors.put("bookId", "Livro já está em uso");

        // Se houver erros de validação, lança exceção
        if (!fieldErrors.isEmpty())
            throw new InputValidationException("Field validation errors", fieldErrors);

        // Mapeia os detalhes do empréstimo para o objeto lease
        lease.setUser(user.get());
        lease.setBook(book.get());
        lease.setLeaseDate(leaseDetails.getLeaseDate());
        lease.setReturnDate(leaseDetails.getReturnDate());
        lease.setStatus(Lease.Status.valueOf(leaseDetails.getStatus()));
    }

    @Override
    public Optional<Lease> getLease(Long id) {
        log.info("Getting lease with id {}", id);
        return leaseRepository.findById(id);
    }

    @Override
    public Page<Lease> getAllLeases(int page, int size) {
        log.info("Getting all leases");
        return leaseRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public boolean deleteLease(Long id) {
        log.info("Deleting lease with id {}", id);
        return leaseRepository.deleteByIdInt(id) > 0;
    }
}
