package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("delete from User u where u.id = :id")
    int deleteByIdInt(@NonNull Long id);
}
