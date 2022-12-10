package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private Long idMemory = 0L;

    private Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        idMemory++;
        user.setId(idMemory);
        users.put(idMemory, user);
        return users.get(user.getId());
    }

    @Override
    public User update(Long id, UserDto userDto) {
        if (userDto.getName() != null) {
            users.get(id).setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            users.get(id).setEmail(userDto.getEmail());
        }
        return users.get(id);
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }
}
