package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users/{userId}")
    public UserDto get(@PathVariable("userId") Long id) {
        log.info("Получение пользователя id {}", id);
        return userMapper.toDTO(userService.getUserById(id));
    }

    @PostMapping("/users")
    public UserDto create(@RequestBody User user) {
        log.info("Создание пользователя {}", user.getName());
        UserDto userDto = userMapper.toDTO(userService.create(user));
        return userDto;
    }

    @PatchMapping("/users/{userId}")
    public UserDto patch(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        log.info("Обновлеие данных пользователя id {}", id);
        userService.getUserById(id);
        return userMapper.toDTO(userService.update(id, userDto));
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable("userId") Long id) {
        log.info("Удаление пользователя id {}", id);
        userService.delete(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Получение всех пользователей");
        return userService.getAllUsers();
    }
}
