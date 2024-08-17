package com.guipadovan.librarymanager.services.impl;

import com.guipadovan.librarymanager.dtos.UserDto;
import com.guipadovan.librarymanager.entities.User;
import com.guipadovan.librarymanager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldReturnCreatedUser_WhenInputIsValid() {
        UserDto userDto = new UserDto("Usuário Teste", "usuario@teste.com", LocalDate.now(), "(44) 91234-5678");
        User userEntity = new User(userDto.getName(), userDto.getEmail(), userDto.getRegistrationDate(), userDto.getPhone());

        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        User createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getName(), createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenIdExists() {
        Long userId = 1L;
        UserDto userDto = new UserDto("Usuária Teste", "usuaria@teste.com", LocalDate.now(), "(44) 98765-4321");
        User existingUser = new User("Usuário Teste", "usuario@teste.com", LocalDate.now(), "(44) 91234-5678");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, userDto);

        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUser_ShouldReturnUser_WhenIdExists() {
        Long userId = 1L;
        User userEntity = new User("Usuário Teste", "usuario@teste.com", LocalDate.now(), "(44) 91234-5678");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Optional<User> user = userService.getUser(userId);

        assertTrue(user.isPresent());
        assertEquals(userEntity.getName(), user.get().getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        int page = 0;
        int size = 10;
        Page<User> userPage = new PageImpl<>(Arrays.asList(
                new User("Usuário Teste", "usuario@teste.com", LocalDate.now(), "(44) 91234-5678"),
                new User("Usuária Teste", "usuaria@teste.com", LocalDate.now(), "(44) 98765-4321")
        ));

        when(userRepository.findAll(PageRequest.of(page, size))).thenReturn(userPage);

        Page<User> users = userService.getAllUsers(page, size);

        assertNotNull(users);
        assertEquals(2, users.getNumberOfElements());
        verify(userRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    void deleteUser_ShouldReturnTrue_WhenIdExists() {
        Long userId = 1L;

        when(userRepository.deleteByIdInt(userId)).thenReturn(1);

        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);
        verify(userRepository, times(1)).deleteByIdInt(userId);
    }

    @Test
    void deleteUser_ShouldReturnFalse_WhenIdDoesNotExist() {
        Long userId = 999L;

        when(userRepository.deleteByIdInt(userId)).thenReturn(0);

        boolean isDeleted = userService.deleteUser(userId);

        assertFalse(isDeleted);
        verify(userRepository, times(1)).deleteByIdInt(userId);
    }
}
