package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
