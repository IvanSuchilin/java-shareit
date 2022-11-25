package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private Long idMemory = 0L;

    private Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public User createUser(User user) {
        idMemory++;
        User newUser = user;
        newUser.setId(idMemory);
        users.put(idMemory, newUser);
        return users.get(newUser.getId());
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public UserDto findUserById(int userId) {
        return null;
    }

    @Override
    public void deleteUserById(int userId) {

    }

    @Override
    public User update(User user) {
        return null;
    }
}
