package com.guipadovan.librarymanager.repositories;

import com.guipadovan.librarymanager.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
