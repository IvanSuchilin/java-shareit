package ru.practicum.shareit.item.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class ItemMapperTest {

    private Item itemTest;
    private ItemCreatingDto itemCreatingDtoTest;
    private ItemDto itemDto;

    @BeforeEach
    void setup() {
        itemTest = new Item();
        itemTest.setOwner(new User(1L, "name", "@mail"));
        itemTest.setName("name");
        itemTest.setDescription("description");
        itemDto = new ItemDto();
        itemDto.setName("itemDtoName");
        itemCreatingDtoTest =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
    }

    @Test
    void toDTOTest() {
        ItemDto itemDto = ItemMapper.INSTANCE.toDTO(itemTest);

        Assertions.assertEquals("name", itemDto.getName());
        Assertions.assertEquals("description", itemDto.getDescription());
        Assertions.assertEquals("@mail", itemDto.getOwner().getEmail());
    }

    @Test
    void toItemTest() {
        Item item = ItemMapper.INSTANCE.toItem(itemCreatingDtoTest);
        Assertions.assertEquals("itemName", item.getName());
        Assertions.assertEquals("itemDescription", item.getDescription());
    }

    @Test
    void toCreatingDTOTest() {
        ItemCreatingDto itemCreatingDto = ItemMapper.INSTANCE.toCreatingDTO(itemTest);
        Assertions.assertEquals("name", itemCreatingDto.getName());
        Assertions.assertEquals("description", itemCreatingDto.getDescription());
    }

    @Test
    void updateItemTest() {
        ItemMapper.INSTANCE.updateItem(itemDto, itemTest);
        Assertions.assertEquals("itemDtoName", itemTest.getName());
    }

    @Test
    void toCommentTest() {
        CommentDto commentDto = new CommentDto(1L, "text", "user", LocalDateTime.now());
        Comment comment = ItemMapper.INSTANCE.toComment(commentDto);
        Assertions.assertEquals("text", comment.getText());
    }

    @Test
    void toCommentDto() {
        Comment comment = new Comment(1L, "text", new Item(), new User(), LocalDateTime.now());
        CommentDto commentDto = ItemMapper.INSTANCE.toCommentDto(comment);
        Assertions.assertEquals("text", commentDto.getText());
    }
}