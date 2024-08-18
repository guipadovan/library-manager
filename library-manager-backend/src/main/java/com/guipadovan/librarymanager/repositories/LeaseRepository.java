package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.entities.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

public interface LeaseRepository extends JpaRepository<Lease, Long> {
    @Query("select l from Lease l where l.book.id = :id and l.status = 'ACTIVE'")
    Lease findByBook_IdAndStatusActive(@Param("id") Long id);

    @Query("select (count(l) > 0) from Lease l where l.book.id = :id and l.status = 'ACTIVE'")
    boolean existsByBook_IdAndStatusActive(@Param("id") @NonNull Long id);

    @Query("select l.book from Lease l where l.user.id = :userId order by l.leaseDate desc")
    List<Book> findAllBooksLeasedByUserOrderByLeaseDateDesc(@Param("userId") @NonNull Long userId);
}
