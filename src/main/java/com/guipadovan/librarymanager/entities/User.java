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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "telefone", nullable = false)
    private String phone;

    public User(String name, String email, LocalDate registrationDate, String phone) {
        this.name = name;
        this.email = email;
        this.registrationDate = registrationDate;
        this.phone = phone;
    }
}
