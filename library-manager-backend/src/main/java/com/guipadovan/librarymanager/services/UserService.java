package com.guipadovan.librarymanager.services;

import com.guipadovan.librarymanager.dtos.UserDto;
import com.guipadovan.librarymanager.entities.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDetails the user DTO containing the user details
     *
     * @return the created User entity
     */
    User createUser(UserDto userDetails);

    /**
     * Updates an existing user.
     *
     * @param id          the ID of the user to update
     * @param userDetails the user DTO containing the updated user details
     *
     * @return the updated User entity
     */
    User updateUser(Long id, UserDto userDetails);

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve
     *
     * @return an Optional containing the user if found, or an empty Optional if not found
     */
    Optional<User> getUser(Long id);

    /**
     * Retrieves a page of users.
     *
     * @param page the page number to retrieve (starting from 0)
     * @param size the number of users per page
     *
     * @return a page of User objects
     */
    Page<User> getAllUsers(int page, int size);

    /**
     * Deletes a user by his ID.
     *
     * @param id the ID of the user to delete
     *
     * @return true if the user was successfully deleted, false otherwise
     */
    boolean deleteUser(Long id);
}
