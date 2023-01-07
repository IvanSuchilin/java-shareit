package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
    class CommentItemDtoSerializationTest {
    @Autowired
    private JacksonTester<CommentDto> jacksonTester;
    @Autowired
    private JacksonTester<ResponseItemDto> jacksonTesterResponseItemDto;
    private ResponseItemDto responseItemDto;
    private CommentDto commentDto;
    @BeforeEach
    void setUp() {
        commentDto = new CommentDto(1L, "text", "author",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        responseItemDto = new ResponseItemDto(1L, "name", "description", true,
                1L, 1L);
    }

    @SneakyThrows
    @Test
    void commentDtoSerializationTest(){
        JsonContent<CommentDto> json = jacksonTester.write(commentDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(json).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo(commentDto.getCreated().toString());
    }

    @SneakyThrows
    @Test
    void responseItemDtoSerializationTest(){
        JsonContent<ResponseItemDto> json = jacksonTesterResponseItemDto.write(responseItemDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(json).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}
