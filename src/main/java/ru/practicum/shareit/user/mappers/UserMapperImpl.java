package ru.practicum.shareit.user.mappers;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return userDto;
    }
}