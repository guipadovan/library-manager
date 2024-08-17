package com.guipadovan.librarymanager.controllers;

import com.guipadovan.librarymanager.dtos.UserDto;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for managing users.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     *
     * @param userDetails the details of the user to create
     *
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDetails) {
        User createdUser = userService.createUser(userDetails);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     *
     * @return the user with the given ID, if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(User.class, id.toString());
        }
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of users per page (default is 10)
     *
     * @return a paginated list of users
     */
    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<User> usersPage = userService.getAllUsers(page, size);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    /**
     * Updates the details of an existing user.
     *
     * @param id          the ID of the user to update
     * @param userDetails the new details for the user
     *
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     *
     * @return a response entity with no content if the user was deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new EntityNotFoundException(User.class, id.toString());
        }
    }
}
