package ru.practicum.shareit.user.mappers;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserMapper {
    UserDto toDTO(User user);
}
