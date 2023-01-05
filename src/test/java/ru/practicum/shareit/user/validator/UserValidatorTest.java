package ru.practicum.shareit.user.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.userExceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.userExceptions.UserEmptyNameException;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class UserValidatorTest {
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void validateUserTestWithEmptyName(){
        User user = new User(1L, "", "@mail");
        UserEmptyNameException thrown = Assertions.assertThrows(UserEmptyNameException.class,
                () -> userValidator.validateUser(user));
        Assertions.assertEquals("Имя не может быть пустым" , thrown.getMessage());
    }

    @Test
    void validateUserTestWithEmptyEmail(){
        User user = new User(1L, "name", "");
        InvalidEmailException thrown = Assertions.assertThrows(InvalidEmailException.class,
                () -> userValidator.validateUser(user));
        Assertions.assertEquals("Адрес электронной почты не может быть пустым и должен содержать символ @" , thrown.getMessage());
    }
}