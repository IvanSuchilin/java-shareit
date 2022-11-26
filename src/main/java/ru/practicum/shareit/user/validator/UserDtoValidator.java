package ru.practicum.shareit.user.validator;

import exceptions.userExceptions.InvalidEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;


@Slf4j
@Component
public class UserDtoValidator {
    public void validateUserDto(UserDto userDto) {
        if (!userDto.getEmail().contains("@")) {
            log.error("Адрес электронной должен содержать символ @");
            throw new InvalidEmailException("Адрес электронной должен содержать символ @");
        }
    }
}
