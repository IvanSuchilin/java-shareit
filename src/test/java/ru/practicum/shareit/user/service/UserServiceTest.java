package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.userExceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.userExceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.userExceptions.UserEmptyNameException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserDtoValidator;
import ru.practicum.shareit.user.validator.UserValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserValidator userValidator;
    @Mock
    UserDtoValidator userDtoValidator;
    @InjectMocks
    UserService userService;

    @Test
    void createUser() {
        User newUser = new User(null, "Test", "test@mail.ru");
        when (userRepository.save(newUser)).thenReturn(newUser);
        UserDto userStored = UserMapper.INSTANCE.toDto(userRepository.save(newUser));

        assertEquals(newUser.getName(), userStored.getName());
        assertEquals(newUser.getEmail(), userStored.getEmail());
    }

    @Test
    void createWrongUserName(){
        User newWrongUser = new User(null, "", "test@mail.ru");
        doThrow(UserEmptyNameException.class)
        .when(userValidator).validateUser(newWrongUser);

        assertThrows(UserEmptyNameException.class,
                ()->userService.create(newWrongUser));
        verify(userRepository, never()).save(newWrongUser);
        verify(userRepository, times(0)).save(newWrongUser);
    }

    @Test
    void createWrongUserEmail(){
        User newWrongUser = new User(null, "Test", "testmail.ru");
        doThrow(InvalidEmailException.class)
                .when(userValidator).validateUser(newWrongUser);

        assertThrows(InvalidEmailException.class,
                ()->userService.create(newWrongUser));
        verify(userRepository, never()).save(newWrongUser);
        verify(userRepository, times(0)).save(newWrongUser);
    }

    @Test
    void createAlreadyExistUserEmail(){
        User newWrongUser = new User(null, "Test", "test@mail.ru");
        doThrow(EmailAlreadyExistException.class)
                .when(userRepository).save(newWrongUser);

        assertThrows(EmailAlreadyExistException.class,
                ()->userService.create(newWrongUser));
        verify(userRepository, times(1)).save(newWrongUser);
    }

    @Test
    void getUserById() {
        User user = new User(1L, "Tes", "test@mail");
        when (userRepository.getReferenceById(user.getId()))
                .thenReturn(user);

        User storedUser = userRepository.getReferenceById(user.getId());

        assertEquals(user.getEmail(), storedUser.getEmail());
        assertEquals(user.getName(), storedUser.getName());
    }

    @Test
    void getUserByWrongId() {
        User user = new User(100L, "Tes", "test@mail");
        doThrow(UserNotFoundException.class)
                .when(userRepository).findAll();

        assertThrows(UserNotFoundException.class,
                ()->userService.getUserById(user.getId()));
        verify(userRepository, never()).getReferenceById(user.getId());
        verify(userRepository, times(0)).getReferenceById(user.getId());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAllUsers() {
    }
}