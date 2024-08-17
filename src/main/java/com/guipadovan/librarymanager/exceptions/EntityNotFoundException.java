package com.guipadovan.librarymanager.exceptions;

/**
 * Custom runtime exception to indicate that a requested entity, identified by class type and specific identifier, was not found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with a detailed message.
     *
     * @param clazz      the class type of the entity that was not found.
     * @param identifier the identifier of the entity that was not found.
     */
    public EntityNotFoundException(Class<?> clazz, String identifier) {
        super(clazz.getSimpleName() + " with id " + identifier + " not found.");
    }

}
