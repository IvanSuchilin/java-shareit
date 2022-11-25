package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.UserValidator;


import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserValidator userValidator;
    private final UserStorage userStorage;

    public User create(User user) {
        userValidator.validateUser(user);
        return userStorage.createUser(user);
    }

    public User update(User user) {
        return null;
    }

    public User getUser(Long id) {
        return null;
    }

    public void delete(Long id) {
    }

    public Collection<User> findAll() {
        return null;
    }
}
