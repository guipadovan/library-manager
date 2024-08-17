package com.guipadovan.librarymanager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_loan_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id", nullable = false, foreignKey = @ForeignKey(name = "fk_loan_book"))
    private Book book;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate loanDate;

    @Column(name = "data_devolucao", nullable = false)
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public enum Status {
        ACTIVE, RETURNED
    }

    public Loan(User user, Book book, LocalDate loanDate, LocalDate returnDate, Status status) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }
}
