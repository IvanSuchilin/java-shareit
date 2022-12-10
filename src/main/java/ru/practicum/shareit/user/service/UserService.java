package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.userExceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validator.UserDtoValidator;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final UserDtoValidator userDtoValidator;
    private final UserStorage userStorage;

    public User create(User user) {
        userValidator.validateUser(user);
        log.debug("Получен запрос на создание пользователя {}", user.getName());
        if (userStorage.getAllUsers()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new EmailAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        return userStorage.createUser(user);
    }

    public UserDto getUserById(Long id) {
        log.debug("Получен запрос GET /users/{userId}");
        if (userStorage.getAllUsers()
                .stream()
                .noneMatch(u -> Objects.equals(u.getId(), id))) {
            throw new UserNotFoundException("Нет такого id");
        }
        return userMapper.toDTO(userStorage.getUserById(id));
    }

    public UserDto update(Long id, UserDto userDto) {
        log.debug("Получен запрос PATCH /users/{userId}");
        if (userDto.getEmail() != null) {
            userDtoValidator.validateUserDto(userDto);
            if (userStorage.getAllUsers()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
                throw new EmailAlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }
        return userMapper.toDTO(userStorage.update(id, userDto));
    }

    public void delete(Long id) {
        log.debug("Получен запрос DELETE /users/{userId}");
        userStorage.deleteUserById(id);
    }

    public Collection<UserDto> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return userStorage.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
