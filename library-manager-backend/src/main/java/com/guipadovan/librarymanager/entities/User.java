package com.guipadovan.librarymanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @NotNull(message = "O ID não deve ser nulo")
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotNull(message = "O nome não deve ser nulo")
    private String name;

    @Column(name = "email", nullable = false)
    @Email(message = "O e-mail deve ser válido")
    @NotNull(message = "O e-mail não deve ser nulo")
    private String email;

    @Column(name = "data_cadastro", nullable = false)
    @PastOrPresent(message = "A data de cadastro deve estar no passado ou no presente")
    @NotNull(message = "A data de cadastro não deve ser nula")
    private LocalDate registrationDate;

    @Column(name = "telefone", nullable = false)
    @NotNull(message = "O telefone não deve ser nulo")
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Lease> leases = new ArrayList<>();

    public User(String name, String email, LocalDate registrationDate, String phone) {
        this.name = name;
        this.email = email;
        this.registrationDate = registrationDate;
        this.phone = phone;
    }
}
