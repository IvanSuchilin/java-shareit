package ru.practicum.shareit.user.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void toDto() {
        User user = new User(1L, "name", "e@mail");
        UserDto userDto = UserMapper.INSTANCE.toDto(user);

        assertEquals(userDto.getName(), user.getName());
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto(1L, "nameNew", "e@mail");
        User user = new User(1L, "name", "e@mail");
        UserMapper.INSTANCE.updateUser(userDto, user);

        assertEquals(user.getName(), "nameNew");
    }
}