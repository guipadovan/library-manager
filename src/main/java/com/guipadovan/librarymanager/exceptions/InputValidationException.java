package com.guipadovan.librarymanager.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Exception thrown to indicate validation errors in input.
 * Should be used when the error needs to be shown in the form.
 * <p>
 * Annotated with @ResponseStatus to return a 400 Bad Request status.
 */
@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InputValidationException extends RuntimeException {

    /**
     * Map containing field-specific validation error messages.
     */
    private final Map<String, String> fieldErrors;

    /**
     * Constructs a new InputValidationException with the specified detail message and field errors.
     *
     * @param message     the detail message.
     * @param fieldErrors a map of field errors.
     */
    public InputValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
}
