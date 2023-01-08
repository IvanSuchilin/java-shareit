package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BookingExceptions.ValidationFailedException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntTest {
    private static User user1;
    private static User user2;
    private static ItemCreatingDto itemCreatingDto;

    private static ItemRequestCreatingDto itemRequestCreatingDto;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemRequestService itemRequestService;

    @BeforeAll
    public static void setup() {
        user1 = new User(null, "name1", "emailtest1@mail.ru");

        itemRequestCreatingDto = new ItemRequestCreatingDto();
        user2 = new User(null, "name2", "email2test@mail.ru");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTest() {
        userService.create(user1);
        itemRequestService.create(1L, itemRequestCreatingDto);
        itemCreatingDto.setRequestId(1L);
        ItemCreatingDto stored = itemService.create(1L, itemCreatingDto);

        assertEquals(stored.getId(), 1L);
        assertEquals(stored.getName(), "itemName");
        assertEquals(stored.getAvailable(), true);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTestWithWrongUserTest() {
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class,
                () -> itemService.create(1L, itemCreatingDto));
        assertEquals("Нет такого id", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTestWithWrongRequestTest() {
        userService.create(user1);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemCreatingDto.setRequestId(1L);
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> itemService.create(1L, itemCreatingDto));
        assertEquals("Запроса c id1 нет", thrown.getReason());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }


    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getItemByIdTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);

        ItemDto stored = itemService.getItemById(1L, 2L);

        assertEquals(stored.getId(), 1L);
        assertEquals(stored.getName(), "itemName");
        assertEquals(stored.getAvailable(), true);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getItemByIdTestItemWithBookingTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);

        ItemDto stored = itemService.getItemById(1L, 1L);

        assertEquals(stored.getId(), 1L);
        assertEquals(stored.getName(), "itemName");
        assertEquals(stored.getAvailable(), true);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getItemByIdTestWrongIdTest() {
        userService.create(user1);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> itemService.getItemById(10L, 1L));
        assertEquals("Предмета c id10 нет", thrown.getReason());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateWrongOwnerTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        ItemDto itemDtoUpd = new ItemDto();
        itemDtoUpd.setName("updName");

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class,
                () -> itemService.update(1L, 2L, itemDtoUpd));
        assertEquals("Нет такого владельца вещи", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateWrongIdTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        ItemDto itemDtoUpd = new ItemDto();
        itemDtoUpd.setName("updName");

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> itemService.update(10L, 2L, itemDtoUpd));
        assertEquals("Предмета c id10 нет", thrown.getReason());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateTest() {
        userService.create(user1);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        ItemDto itemDtoUpd = new ItemDto();
        itemDtoUpd.setName("updName");

        ItemDto updated = itemService.update(1L, 1L, itemDtoUpd);

        assertEquals("updName", updated.getName());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getAllUsersItemsTests() {
        userService.create(user1);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        Collection<ItemDto> items = itemService.getAllUsersItems(1L, null);

        assertEquals(1, items.size());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void searchItemTest() {
        userService.create(user1);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        List<ItemDto> items = (List<ItemDto>) itemService.searchItem("itemName", null);

        assertEquals(1, items.size());
        assertEquals("itemName", items.get(0).getName());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void searchItemEmptyTextTest() {
        userService.create(user1);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        List<ItemDto> items = (List<ItemDto>) itemService.searchItem("", null);

        assertEquals(0, items.size());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createEmptyCommentTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        CommentDto commentDto = new CommentDto(null, "", user2.getName(), LocalDateTime.now());

        ValidationFailedException thrown = assertThrows(ValidationFailedException.class,
                () -> itemService.createComment(2L, 1L, commentDto));
        assertEquals("Комментарий не может быть пустым", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createWrongItemIdCommentTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        CommentDto commentDto = new CommentDto(null, "comment", user2.getName(), LocalDateTime.now());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> itemService.createComment(2L, 10L, commentDto));
        assertEquals("Предмета c id10 нет", thrown.getReason());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createWrongBookingTimeCommentTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        CommentDto commentDto = new CommentDto(null, "comment", user2.getName(), LocalDateTime.now());

        ValidationFailedException thrown = assertThrows(ValidationFailedException.class,
                () -> itemService.createComment(2L, 1L, commentDto));
        assertEquals("Бронирование пользователя не завершено или не существует", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createCommentTest() {
        userService.create(user1);
        userService.create(user2);
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemService.create(1L, itemCreatingDto);
        bookingService.create(2L,
                new BookingCreateDto(1L, LocalDateTime.now(),
                        LocalDateTime.now().plusNanos(1L), user2));
        CommentDto commentDto = new CommentDto(null, "comment", user2.getName(),
                LocalDateTime.of(2023, 3, 2, 5, 5, 5));

        CommentDto stored = itemService.createComment(2L, 1L, commentDto);

        assertEquals("comment", stored.getText());
    }
}