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
public class BookDto {

    @NotNull(message = "O título não deve ser nulo")
    private String title;

    @NotNull(message = "O autor não deve ser nulo")
    private String author;

    @NotNull(message = "O ISBN não deve ser nulo")
    @Pattern(regexp = "\\d{13}", message = "O ISBN deve conter 13 dígitos")
    private String isbn;

    @NotNull(message = "A data de publicação não deve ser nula")
    @PastOrPresent(message = "A data de publicação deve estar no passado ou no presente")
    private LocalDate publicationDate;

    @NotNull(message = "A categoria não deve ser nula")
    private String category;

}
