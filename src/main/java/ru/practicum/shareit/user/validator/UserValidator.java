package ru.practicum.shareit.user.validator;

import ru.practicum.shareit.exceptions.userExceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.userExceptions.UserEmptyNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class UserValidator {
    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Адрес электронной почты не может быть пустым и должен содержать символ @");
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым и должен содержать символ @");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn("Имя пользователя не может быть пустым");
            throw new UserEmptyNameException("Имя не может быть пустым");
        }
    }
}
