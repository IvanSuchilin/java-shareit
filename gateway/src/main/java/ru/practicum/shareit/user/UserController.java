package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping
public class UserController {
    private final UserClient userClient;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> get(@Positive @PathVariable("userId") Long id) {
        log.info("Получение пользователя id в getway {}", id);
        return userClient.getUserById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> create(@RequestBody UserDto user) {
        log.info("Создание пользователя в getway {}", user.getName());
        return userClient.createUser(user);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Object> patch(@Positive @PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        log.info("Обновлеие данных пользователя id в getway {}", id);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> delete(@Positive @PathVariable("userId") Long id) {
        log.info("Удаление пользователя id в getway {}", id);
        return userClient.deleteUser(id);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> findAll() {
        log.info("Получение всех пользователей в getway");
        return userClient.getAllUsers();
    }
}
