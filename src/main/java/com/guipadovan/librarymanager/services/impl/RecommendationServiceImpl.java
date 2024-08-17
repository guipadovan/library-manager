package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.LeaseService;
import com.guipadovan.librarymanager.services.RecommendationService;
import com.guipadovan.librarymanager.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final UserService userService;
    private final BookService bookService;
    private final LeaseService leaseService;

    public RecommendationServiceImpl(UserService userService, BookService bookService, LeaseService leaseService) {
        this.userService = userService;
        this.bookService = bookService;
        this.leaseService = leaseService;
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if the user is not found
     */
    @Override
    public List<Book> getBookRecommendationsByUser(Long userId, int limit) throws EntityNotFoundException {
        // Busca o usuário pelo ID, lança exceção se não encontrado
        userService.getUser(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId.toString()));

        // Busca todos os livros emprestados pelo usuário e cria os filtros para buscar livros recomendados
        List<Book> usersLeasedBooks = leaseService.getLeasedBooksByUser(userId);
        List<String> categories = usersLeasedBooks.stream().map(Book::getCategory).distinct().toList();
        List<Long> excludedBookIds = usersLeasedBooks.stream().map(Book::getId).toList();

        List<Book> books = bookService.getBookRecommendations(categories, excludedBookIds, limit);

        log.info("Getting book recommendations for user with id {}", userId);
        return books;
    }

}
