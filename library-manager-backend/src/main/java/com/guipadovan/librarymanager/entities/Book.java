package com.guipadovan.librarymanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @NotNull(message = "O ID não deve ser nulo")
    private Long id;

    @Column(name = "titulo", nullable = false)
    @NotNull(message = "O título não deve ser nulo")
    private String title;

    @Column(name = "autor", nullable = false)
    @NotNull(message = "O autor não deve ser nulo")
    private String author;

    @Column(name = "isbn", nullable = false)
    @NotNull(message = "O ISBN não deve ser nulo")
    private String isbn;

    @Column(name = "data_publicacao", nullable = false)
    @NotNull(message = "A data de publicação não deve ser nula")
    private LocalDate publicationDate;

    @Column(name = "categoria", nullable = false)
    @NotNull(message = "A categoria não deve ser nula")
    private String category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Lease> leases = new ArrayList<>();

    public Book(String title, String author, String isbn, LocalDate publicationDate, String category) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.category = category;
    }
}
