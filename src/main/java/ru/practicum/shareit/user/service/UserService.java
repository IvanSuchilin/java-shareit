package ru.practicum.shareit.user.service;

import exceptions.userExceptions.EmailAlreadyExistException;
import exceptions.userExceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validator.UserDtoValidator;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserValidator userValidator;
    private final UserDtoValidator userDtoValidator;
    private final UserStorage userStorage;

    public User create(User user) {
        userValidator.validateUser(user);
        User newUser = null;
        if (userStorage.getAllUsers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new EmailAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        return userStorage.createUser(user);
    }

    public User getUserById(Long id) {
        log.debug("Получен запрос GET /users/{userId}");
        if (!userStorage.getAllUsers().stream().anyMatch(u -> u.getId() == id)) {
            throw new UserNotFoundException("Нет такого id");
        }
        return userStorage.getUserById(id);
    }

    public User update(Long id, UserDto userDto) {
        if (userDto.getEmail() != null) {
            userDtoValidator.validateUserDto(userDto);
            if (userStorage.getAllUsers().stream().anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
                throw new EmailAlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }
        return userStorage.update(id, userDto);
    }

    public User getUser(Long id) {
        return null;
    }

    public void delete(Long id) {
        userStorage.deleteUserById(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}
