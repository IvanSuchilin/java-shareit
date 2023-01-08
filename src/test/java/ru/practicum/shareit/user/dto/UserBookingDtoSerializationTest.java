package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserBookingDtoSerializationTest {
    @Autowired
    private JacksonTester<UserBookingDto> jacksonTesterUserBookingDto;
    @Autowired
    private JacksonTester<UserDto> jacksonTesterUserDto;
    private UserBookingDto userBookingDto;
    private  UserDto userDto;

    @BeforeEach
    void setUp() {
        userBookingDto = new UserBookingDto(1L, "name");
        userDto = new UserDto(1L, "name", "e@mail");
    }

    @SneakyThrows
    @Test
    void userBookingDtoSerializationTest() {
        JsonContent<UserBookingDto> json = jacksonTesterUserBookingDto.write(userBookingDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(userBookingDto.getName());
    }

    @SneakyThrows
    @Test
    void userDtoSerializationTest() {
        JsonContent<UserDto> json = jacksonTesterUserDto.write(userDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }
}