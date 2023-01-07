package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestSerializationDtoTest {
    @Autowired
    private JacksonTester<ItemRequestCreatingDto> jacksonTesterItemRequestCreatingDto;
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTesterItemRequestDto;
    @Autowired
    private JacksonTester<RequestResponseDto> jacksonTesterRequestResponseDto;
    private ItemRequestCreatingDto itemRequestCreatingDto;
    private ItemRequestDto itemRequestDto;
    private RequestResponseDto requestResponseDto;
    @BeforeEach
    void setUp() {
        itemRequestCreatingDto = new ItemRequestCreatingDto("description");
        itemRequestDto = new ItemRequestDto(1L, "description", new User(1L, "userName",
                "e@mail"), LocalDateTime.now());
        requestResponseDto = new RequestResponseDto(1L, "description", LocalDateTime.now(), null);
    }

    @SneakyThrows
    @Test
    void itemRequestCreatingDtoSerializationTest() {
        JsonContent<ItemRequestCreatingDto> json = jacksonTesterItemRequestCreatingDto.write(itemRequestCreatingDto);

        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @SneakyThrows
    @Test
    void itemRequestDtoSerializationTest() {
        JsonContent<ItemRequestDto> json = jacksonTesterItemRequestDto.write(itemRequestDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(json).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.requester.name").isEqualTo("userName");
    }
    @SneakyThrows
    @Test
    void requestResponseDtoSerializationTest() {
        JsonContent<RequestResponseDto> json = jacksonTesterRequestResponseDto.write(requestResponseDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }
}