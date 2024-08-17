package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.UserDto;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.exceptions.EntityNotFoundException;
import com.guipadovan.librarymanager.repositories.UserRepository;
import com.guipadovan.librarymanager.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserService} interface.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDto userDetails) {
        // Mapeia os detalhes do usuário para o objeto user
        User userEntity = new User(userDetails.getName(), userDetails.getEmail(), userDetails.getRegistrationDate(), userDetails.getPhone());

        log.info("Creating user {}", userEntity);
        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if the user is not found
     */
    @Override
    public User updateUser(Long id, UserDto userDetails) {
        // Busca o usuário pelo ID, lança exceção se não encontrado
        User userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id.toString()));

        // Mapeia os detalhes do usuário para o objeto user
        userEntity.setName(userDetails.getName());
        userEntity.setEmail(userDetails.getEmail());
        userEntity.setRegistrationDate(userDetails.getRegistrationDate());
        userEntity.setPhone(userDetails.getPhone());

        log.info("Updating user {}", userEntity);
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<User> getUser(Long id) {
        log.info("Getting user with id {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        log.info("Getting all users");
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public boolean deleteUser(Long id) {
        log.info("Deleting user with id {}", id);
        return userRepository.deleteByIdInt(id) > 0;
    }
}
