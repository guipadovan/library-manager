package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.LeaseDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.Lease;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.InputValidationException;
import com.guipadovan.librarymanager.repositories.LeaseRepository;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.LeaseService;
import com.guipadovan.librarymanager.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link LeaseService} interface.
 */
@Service
@Transactional
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
        lease.setStatus(Lease.Status.ACTIVE);

        log.info("Creating lease {}", lease);
        return leaseRepository.save(lease);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InputValidationException if there are validation errors
     */
    @Override
    public Lease returnBook(Long id) throws InputValidationException {
        HashMap<String, String> fieldErrors = new HashMap<>();

        // Valida o livro
        Optional<Book> book = bookService.getBook(id);
        if (book.isEmpty())
            fieldErrors.put("bookId", "Livro não encontrado");
        else if (!leaseRepository.existsByBook_IdAndStatusActive(id))
            fieldErrors.put("bookId", "Livro não está em uso");

        // Se houver erros de validação, lança exceção
        if (!fieldErrors.isEmpty())
            throw new InputValidationException("Field validation errors", fieldErrors);

        Lease lease = leaseRepository.findByBook_IdAndStatusActive(id);

        lease.setStatus(Lease.Status.RETURNED);
        lease.setReturnDate(LocalDate.now());

        log.info("Updating lease {}", lease);
        return leaseRepository.save(lease);
    }

    @Override
    public List<Book> getLeasedBooksByUser(Long userId) {
        log.info("Getting leased book IDs for user with id {}", userId);
        return leaseRepository.findAllBooksLeasedByUserOrderByLeaseDateDesc(userId);
    }
}
