package com.teclead.ventures.javatask.service;

import com.teclead.ventures.javatask.dto.RequestUserDto;
import com.teclead.ventures.javatask.dto.ResponseUserDto;
import com.teclead.ventures.javatask.entity.User;
import com.teclead.ventures.javatask.exception.UserNotFoundException;
import com.teclead.ventures.javatask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    void getAllUsers_ReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<ResponseUserDto> users = userService.getAllUsers();

        assertEquals(0, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_ReturnUsers() {
        when(userRepository.findAll()).thenReturn(
                Arrays.asList(generateUser(), generateUser())
        );

        List<ResponseUserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsersByVorname_ReturnEmptyList() {
        String vorname = "test-vorname";

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<ResponseUserDto> users = userService.getAllUsersByVorname(vorname);

        assertEquals(0, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsersByVorname_ReturnTwoUsers() {
        String vorname = "test-vorname";

        when(userRepository.findAll()).thenReturn(
                Arrays.asList(
                        generateUser(),
                        generateUser(),
                        new User(3L, "fremd user", "fremd user", "user@mail.de"))
        );
        List<ResponseUserDto> users = userService.getAllUsersByVorname(vorname);

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void getUserById_ReturnUser() {
        Long testId = 3L;

        when(userRepository.findUserById(testId)).thenReturn(Optional.of(generateUser()));

        ResponseUserDto userDto = userService.getUserById(testId);

        assertEquals("db@mail.test", userDto.getEmail());

        verify(userRepository, times(1)).findUserById(testId);
    }

    @Test
    void getUserById_ThrowUserNotFoundException() {
        Long testId = 3L;

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(testId));

        assertNotNull(exception);
        assertEquals("User with id 3 doesn't exist", exception.getMessage());
    }

    @Test
    void addUser_Success() {
        RequestUserDto userDto = RequestUserDto
                .builder()
                .name("Musterman")
                .vorname("Max")
                .email("max.musterman@mail.de")
                .build();

        User user = User.builder()
                .name("Musterman")
                .vorname("Max")
                .email("max.musterman@mail.de")
                .build();

        when(userRepository.save(user)).thenReturn(user);

        ResponseUserDto responseUserDto = userService.addUser(userDto);

        assertEquals(userDto.getName(), responseUserDto.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_Success() {
        RequestUserDto userDto = RequestUserDto
                .builder()
                .name("Musterman")
                .vorname("Max")
                .email("max.musterman@mail.de")
                .build();

        User user = User.builder()
                .id(3L)
                .name("Musterman")
                .vorname("Max")
                .email("max.musterman@mail.de")
                .build();

        Long testId = 3L;

        when(userRepository.findUserById(testId)).thenReturn(Optional.of(user));

        userService.updateUser(testId, userDto);

        verify(userRepository, times(1)).findUserById(testId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ThrowUserRepositoryException() {
        RequestUserDto userDto = RequestUserDto
                .builder()
                .name("Musterman")
                .vorname("Max")
                .email("max.musterman@mail.de")
                .build();

        Long testId = 3L;

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> userService.updateUser(testId, userDto));

        assertNotNull(exception);
        assertEquals("User with id 3 doesn't exist", exception.getMessage());

        verify(userRepository, times(1)).findUserById(testId);
    }

    @Test
    void deleteUser_Success() {
        Long testId = 3L;

        when(userRepository.findUserById(testId)).thenReturn(Optional.of(generateUser()));

        userService.deleteUser(testId);

        verify(userRepository, times(1)).findUserById(testId);
        verify(userRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteUser_ThrowUserNotFoundException() {
        Long testId = 3L;

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> userService.deleteUser(testId));

        assertNotNull(exception);
        assertEquals("User with id 3 doesn't exist", exception.getMessage());

        verify(userRepository, times(1)).findUserById(testId);
    }


    private ResponseUserDto generateResponseUserDto() {
        return ResponseUserDto.builder()
                .id(1L)
                .name("test name")
                .vorname("test vorname")
                .email("test@mail.test")
                .build();
    }

    private User generateUser() {
        return User.builder()
                .id(1L)
                .name("db user name")
                .vorname("test-vorname")
                .email("db@mail.test")
                .build();
    }
}