package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.userExceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserDtoValidator;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserDtoValidator userDtoValidator;

    public User create(User user) {
        userValidator.validateUser(user);
        log.debug("Получен запрос на создание пользователя {}", user.getName());
        User saveUser;
        try {
            saveUser = userRepository.save(user);
        } catch (Throwable e) {
            throw new EmailAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        return saveUser;
    }

    public UserDto getUserById(Long id) {
        log.debug("Получен запрос GET /users/{userId}");
        if (userRepository.findAll()
                .stream()
                .noneMatch(u -> Objects.equals(u.getId(), id))) {
            throw new UserNotFoundException("Нет такого id");
        }
        return UserMapper.INSTANCE.toDto(userRepository.getReferenceById(id));
    }

    public UserDto update(Long id, UserDto userDto) {
        log.debug("Получен запрос PATCH /users/{userId}");
        if (userDto.getEmail() != null) {
            userDtoValidator.validateUserDto(userDto);
            if (userRepository.findAll()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
                throw new EmailAlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }
        try {
            User stored = userRepository.findById(id)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            UserMapper.INSTANCE.updateUser(userDto, stored);
            return UserMapper.INSTANCE.toDto(userRepository.save(stored));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new UserNotFoundException("Нет такого пользователя");
        }
    }

    public void delete(Long id) {
        log.debug("Получен запрос DELETE /users/{userId}");
        userRepository.deleteById(id);
    }

    public Collection<UserDto> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return userRepository.findAll()
                .stream()
                .map(UserMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
}
