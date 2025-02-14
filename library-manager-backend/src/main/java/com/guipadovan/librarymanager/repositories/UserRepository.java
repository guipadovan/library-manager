package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("delete from User u where u.id = :id")
    int deleteByIdInt(@Param("id") @NonNull Long id);
}
