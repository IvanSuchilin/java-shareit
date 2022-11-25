package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Optional;

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
        return null;
    }

    @PostMapping("/users")
    public UserDto create(@RequestBody User user) {
        log.info("Создание пользователя");
        /*UserDto userdto = UserMapper.INSTANCE.toDTO(userService.create(user));*/
        /*return UserMapper.INSTANCE.toDTO(userService.create(user));*/
        /*return userService.create(user);*/
        UserDto userDto = userMapper.toDTO(userService.create(user));
        return userDto;
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        log.info("Получени евсех пользователей");
        return userService.update(user);
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable("userId") Long id) {
        log.info("Удаление пользователя id {}", id);
        userService.delete(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Получение всех пользователей");
        return userService.findAll();
    }
}
