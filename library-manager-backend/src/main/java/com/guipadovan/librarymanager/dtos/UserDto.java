package com.guipadovan.librarymanager.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = "O nome não deve ser nulo")
    private String name;

    @NotNull(message = "O e-mail não deve ser nulo")
    @Email(message = "O e-mail deve ser válido")
    private String email;

    @NotNull(message = "A data de cadastro não deve ser nula")
    @PastOrPresent(message = "A data de cadastro deve estar no passado ou no presente")
    private LocalDate registrationDate;

    @NotNull(message = "O telefone não deve ser nulo")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String phone;

}
