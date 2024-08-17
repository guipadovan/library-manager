package com.guipadovan.librarymanager.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaseDto {

    @NotNull(message = "O usuário não deve ser nulo")
    private Long userId;

    @NotNull(message = "O livro não deve ser nulo")
    private Long bookId;

    @NotNull(message = "A data de empréstimo não deve ser nula")
    @PastOrPresent(message = "A data de empréstimo deve estar no passado ou no presente")
    private LocalDate leaseDate;

    @NotNull(message = "A data de devolução não deve ser nula")
    @Future(message = "A data de devolução deve estar no futuro")
    private LocalDate returnDate;

}
