package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.Lease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaseRepository extends JpaRepository<Lease, Long> {
}
