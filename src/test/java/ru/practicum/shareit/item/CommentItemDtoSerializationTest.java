package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentItemDtoSerializationTest {
    @Autowired
    private JacksonTester<CommentDto> jacksonTester;
    @Autowired
    private JacksonTester<ItemBookingDto> jacksonTesterItemBookingDto;
    @Autowired
    private JacksonTester<ResponseItemDto> jacksonTesterResponseItemDto;
    @Autowired
    private JacksonTester<ItemCreatingDto> jacksonTesterItemCreatingDto;
    @Autowired
    private JacksonTester<ItemDto> jacksonTesterItemDto;
    private ResponseItemDto responseItemDto;
    private CommentDto commentDto;
    private ItemBookingDto itemBookingDto;
    private ItemCreatingDto itemCreatingDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        commentDto = new CommentDto(1L, "text", "author",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        responseItemDto = new ResponseItemDto(1L, "name", "description", true,
                1L, 1L);
        itemBookingDto = new ItemBookingDto(1L, "name");
        itemCreatingDto = new ItemCreatingDto(1L, "name", "description", true, 2L);
        itemDto = new ItemDto(1L, "name", "description", true, new User(1L, "userName", "e@mail"),
                new ItemRequest(), new BookingItemDto(1L, 1L), null, null);
    }

    @SneakyThrows
    @Test
    void commentDtoSerializationTest() {
        JsonContent<CommentDto> json = jacksonTester.write(commentDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(json).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
    }

    @SneakyThrows
    @Test
    void responseItemDtoSerializationTest() {
        JsonContent<ResponseItemDto> json = jacksonTesterResponseItemDto.write(responseItemDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(json).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void itemBookingDtoSerialization() {
        JsonContent<ItemBookingDto> json = jacksonTesterItemBookingDto.write(itemBookingDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("name");
    }

    @SneakyThrows
    @Test
    void itemCreatingDtoSerializationTest() {
        JsonContent<ItemCreatingDto> json = jacksonTesterItemCreatingDto.write(itemCreatingDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @SneakyThrows
    @Test
    void itemDtoSerializationTest() {
        JsonContent<ItemDto> json = jacksonTesterItemDto.write(itemDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(json).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.owner.name").isEqualTo("userName");
    }
}
