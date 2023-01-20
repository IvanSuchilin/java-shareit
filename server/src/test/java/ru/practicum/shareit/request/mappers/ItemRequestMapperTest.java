package ru.practicum.shareit.request.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

class ItemRequestMapperTest {
    private Item item;

    @BeforeEach
    void setup() {
        User user = new User(null, "name1", "emailtest1@mail.ru");
        item = new Item(1L, "itemName", "description", true, user, null, null);
    }

    @Test
    void toResponseItemDTO() {
        ResponseItemDto responseItemDto = ItemRequestMapper.INSTANCE.toResponseItemDTO(item);

        Assertions.assertEquals(responseItemDto.getAvailable(), item.getAvailable());
        Assertions.assertEquals(responseItemDto.getName(), item.getName());
    }
}