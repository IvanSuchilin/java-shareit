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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}")
    public UserDto get(@PathVariable("userId") Long id) {
        log.info("Получение пользователя id {}", id);
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public UserDto create(@RequestBody User user) {
        log.info("Создание пользователя {}", user.getName());
        return UserMapper.INSTANCE.toDto(userService.create(user));
    }

    @PatchMapping("/users/{userId}")
    public UserDto patch(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        log.info("Обновлеие данных пользователя id {}", id);
        userService.getUserById(id);
        return userService.update(id, userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable("userId") Long id) {
        log.info("Удаление пользователя id {}", id);
        userService.delete(id);
    }

    @GetMapping("/users")
    public Collection<UserDto> findAll() {
        log.info("Получение всех пользователей");
        return userService.getAllUsers();
    }
}
