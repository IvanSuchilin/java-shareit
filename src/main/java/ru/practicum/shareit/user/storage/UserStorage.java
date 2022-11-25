package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    UserDto findUserById(int userId);
    void deleteUserById(int userId);
    User update(User user);
}
