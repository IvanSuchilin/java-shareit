package ru.practicum.shareit.user.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.userExceptions.InvalidEmailException;
import ru.practicum.shareit.user.dto.UserDto;

@SpringBootTest
class UserDtoValidatorTest {
    private UserDtoValidator userDtoValidator;

    @BeforeEach
    void setUp() {
        userDtoValidator = new UserDtoValidator();
    }

    @Test
    void validateUserDtoTest() {
        UserDto userDto = new UserDto(1L, "name", "mail");
        InvalidEmailException thrown = Assertions.assertThrows(InvalidEmailException.class,
                () -> userDtoValidator.validateUserDto(userDto));
        Assertions.assertEquals("Адрес электронной должен содержать символ @",
                thrown.getMessage());
    }
}