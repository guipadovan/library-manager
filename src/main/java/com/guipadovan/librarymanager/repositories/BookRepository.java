package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Transactional
    @Modifying
    @Query("delete from Book b where b.id = :id")
    int deleteByIdInt(@NonNull Long id);

    @Query("select b from Book b where b.category in :categories and b.id not in :excludedBookIds order by function('RAND') asc, b.title asc")
    List<Book> findBooksByCategoriesRandomizedAndOrdered(
            @Param("categories") @NonNull List<String> categories,
            @Param("excludedBookIds") @NonNull List<Long> excludedBookIds,
            Pageable pageable
    );
}
