package com.guipadovan.librarymanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "lease")
public class Lease {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @NotNull(message = "O ID não deve ser nulo")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lease_user"))
    @NotNull(message = "O usuário não deve ser nulo")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lease_book"))
    @NotNull(message = "O livro não deve ser nulo")
    private Book book;

    @Column(name = "data_emprestimo", nullable = false)
    @PastOrPresent(message = "A data de empréstimo deve estar no passado ou no presente")
    @NotNull(message = "A data de empréstimo não deve ser nula")
    private LocalDate leaseDate;

    @Column(name = "data_devolucao", nullable = false)
    @NotNull(message = "A data de devolução não deve ser nula")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "O status não deve ser nulo")
    private Status status;

    public Lease(User user, Book book, LocalDate leaseDate, LocalDate returnDate, Status status) {
        this.user = user;
        this.book = book;
        this.leaseDate = leaseDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public enum Status {
        ACTIVE, RETURNED
    }
}
