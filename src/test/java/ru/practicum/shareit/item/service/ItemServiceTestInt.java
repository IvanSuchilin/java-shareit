package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTestInt {
    private static User user1;
    private static User user2;
    private static ItemCreatingDto itemCreatingDto;

    private static ItemRequestCreatingDto itemRequestCreatingDto;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestService itemRequestService;

    @BeforeAll
    public static void setup() {
        user1 = new User(null, "name1", "emailtest1@mail.ru");
        itemCreatingDto =
                new ItemCreatingDto(null, "itemName", "itemDescription", true, null);
        itemRequestCreatingDto = new ItemRequestCreatingDto();
        user2 = new User(null, "name2", "email2test@mail.ru");
       // wrongUser =  new User(null, "", "");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTest() {
        userService.create(user1);
        itemRequestService.create(1L , itemRequestCreatingDto);
        itemCreatingDto.setRequestId(1L);
        ItemCreatingDto stored = itemService.create(1L, itemCreatingDto);

        assertEquals(stored.getId(), 1L);
        assertEquals(stored.getName(), "itemName");
        assertEquals(stored.getAvailable(), true);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTestWithWrongUser() {
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.create(1L, itemCreatingDto));
        Assertions.assertEquals("Нет такого id", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTestWithWrongRequest() {
        userService.create(user1);
        itemCreatingDto.setRequestId(1L);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> itemService.create(1L, itemCreatingDto));
        Assertions.assertEquals("Запроса c id1 нет", thrown.getReason());
    }


    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getItemByIdTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, itemCreatingDto);

        ItemDto stored = itemService.getItemById(1L,2L);

        assertEquals(stored.getId(), 1L);
        assertEquals(stored.getName(), "itemName");
        assertEquals(stored.getAvailable(), true);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getItemByIdTestWrongId() {
        userService.create(user1);
        itemService.create(1L, itemCreatingDto);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> itemService.getItemById(10L,1L));
        Assertions.assertEquals("Предмета c id10 нет", thrown.getReason());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void update() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, itemCreatingDto);
        ItemDto itemDtoUpd = new ItemDto();
        itemDtoUpd.setName("updName");

        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.update(1L, 2L, itemDtoUpd));
        Assertions.assertEquals("Нет такого владельца вещи", thrown.getMessage());
    }

    @Test
    void getAllUsersItems() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void createComment() {
    }
}