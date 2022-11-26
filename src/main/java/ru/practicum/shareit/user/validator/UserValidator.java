package ru.practicum.shareit.user.validator;

import exceptions.userExceptions.InvalidEmailException;
import exceptions.userExceptions.UserEmptyNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class UserValidator {
    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты не может быть пустым и должен содержать символ @");
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым и должен содержать символ @");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.error("Имя пользователя не может быть пустым");
            throw new UserEmptyNameException("Имя не может быть пустым");
        }
    }
}