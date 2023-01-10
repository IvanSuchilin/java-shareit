package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/test/java/resources/scripts/schemaTest.sql"})
class ItemRequestServiceIntTest {
    private static ItemRequestCreatingDto itemRequestCreatingDto;
    private static User user1;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRequestService itemRequestService;

    @BeforeAll
    public static void setup() {
        user1 = new User(null, "name1", "emailtest1@mail.ru");

        itemRequestCreatingDto = new ItemRequestCreatingDto("text");
    }

    @Test
    void createWrongUserTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> itemRequestService.create(1L, itemRequestCreatingDto));
        Assertions.assertEquals("Пользователя c id1 нет", thrown.getReason());
    }

    @Test
    void createTest() {
        userService.create(user1);
        ItemRequestDto stored = itemRequestService.create(1L, itemRequestCreatingDto);

        assertEquals(stored.getId(), 1L);
        assertEquals("text", stored.getDescription());
        assertEquals(1, stored.getRequester().getId());
    }


    @Test
    void getAllByUserId() {
        userService.create(user1);
        itemRequestService.create(1L, itemRequestCreatingDto);
        List<RequestResponseDto> requests = itemRequestService.getAllByUserId(1L);

        assertEquals(1, requests.size());
        assertEquals("text", requests.get(0).getDescription());
    }

    @Test
    void getRequestByWrongId() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> itemRequestService.getRequestById(1L));
        Assertions.assertEquals("Запроса c id1 нет", thrown.getReason());
    }

    @Test
    void getRequestById() {
        userService.create(user1);
        itemRequestService.create(1L, itemRequestCreatingDto);
        RequestResponseDto stored = itemRequestService.getRequestById(1L);

        assertEquals(stored.getId(), 1L);
        assertEquals("text", stored.getDescription());
    }

    @Test
    void getAllRequestsWithPagination() {
        userService.create(user1);
        itemRequestService.create(1L, itemRequestCreatingDto);
        List<RequestResponseDto> requests = itemRequestService.getAllRequestsWithPagination(0, 10, 2L);
        assertEquals(1, requests.size());
        assertEquals("text", requests.get(0).getDescription());
    }
}