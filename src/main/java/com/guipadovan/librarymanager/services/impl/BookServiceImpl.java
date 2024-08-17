package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.BookDto;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.repositories.BookRepository;
import com.guipadovan.librarymanager.services.BookService;
import com.guipadovan.librarymanager.services.LeaseService;
import com.guipadovan.librarymanager.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link BookService} interface.
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(BookDto bookDetails) {
        // Mapeia os detalhes do livro para o objeto book
        Book bookEntity = new Book(bookDetails.getTitle(), bookDetails.getAuthor(), bookDetails.getIsbn(), bookDetails.getPublicationDate(), bookDetails.getCategory());

        log.info("Creating book {}", bookEntity);
        return bookRepository.save(bookEntity);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if the book is not found
     */
    @Override
    public Book updateBook(Long id, @Valid BookDto bookDetails) {
        // Busca o livro pelo ID, lança exceção se não encontrado
        Book bookEntity = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Book.class, id.toString()));

        // Mapeia os detalhes do livro para o objeto book
        bookEntity.setTitle(bookDetails.getTitle());
        bookEntity.setAuthor(bookDetails.getAuthor());
        bookEntity.setIsbn(bookDetails.getIsbn());
        bookEntity.setPublicationDate(bookDetails.getPublicationDate());
        bookEntity.setCategory(bookDetails.getCategory());

        log.info("Updating book {}", bookEntity);
        return bookRepository.save(bookEntity);
    }

    @Override
    public Optional<Book> getBook(Long id) {
        log.info("Getting book with id {}", id);
        return bookRepository.findById(id);
    }

    @Override
    public Page<Book> getAllBooks(int page, int size) {
        log.info("Getting all books");
        return bookRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Book> getBookRecommendations(List<String> categories, List<Long> excludedBookIds, int limit) {
        return bookRepository.findBooksByCategoriesRandomizedAndOrdered(categories, excludedBookIds, PageRequest.of(0, limit));
    }

    @Override
    public boolean deleteBook(Long id) {
        log.info("Deleting book with id {}", id);
        return bookRepository.deleteByIdInt(id) > 0;
    }
}
