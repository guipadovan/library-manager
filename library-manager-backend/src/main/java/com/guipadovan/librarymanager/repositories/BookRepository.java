package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Query("delete from Book b where b.id = :id")
    int deleteByIdInt(@NonNull Long id);

    @Query("select b from Book b where b.category in :categories and b.id not in :excludedBookIds "
            + "and b.id not in (select l.book.id from Lease l where l.status = 'ACTIVE') "
            + "order by function('RANDOM') asc, b.title asc")
    List<Book> findBooksByCategoriesRandomizedAndOrdered(
            @Param("categories") @NonNull List<String> categories,
            @Param("excludedBookIds") @NonNull List<Long> excludedBookIds,
            Pageable pageable
    );
}
