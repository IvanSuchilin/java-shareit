package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BookingExceptions.ValidationFailedException;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.exceptions.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserBookingDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntTest {
    private static User user1;
    private static User user2;
    private static BookingCreateDto bookingCreateDto;
    private static UserDto userDto;
    private static ItemBookingDto itemBookingDto;
    private static UserBookingDto userBookingDto;
    private static LocalDateTime startBooking;
    private static LocalDateTime endBooking;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @BeforeAll
    public static void setup() {
        user1 = new User(null, "name1", "emailtest1@mail.ru");
        user2 = new User(null, "name2", "email2test@mail.ru");
        userDto = new UserDto(1L, "userName", "e@mail");
        itemBookingDto = new ItemBookingDto(1L, "itemName");
        userBookingDto = new UserBookingDto(1L, "name1");
        startBooking = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
        endBooking = LocalDateTime.now().plusHours(10).truncatedTo(ChronoUnit.SECONDS);
        bookingCreateDto = new BookingCreateDto(1L, startBooking, endBooking, user1);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createWrongUserTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.create(10L, bookingCreateDto));
        Assertions.assertEquals("Пользователя c id10 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createWrongItemTest() {
        userService.create(user1);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.create(1L, bookingCreateDto));
        Assertions.assertEquals("Предмета c id1 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createNotAvailableItemTest() {
        userService.create(user1);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", false, null));

        InvalidItemDtoException thrown = Assertions.assertThrows(InvalidItemDtoException.class,
                () -> bookingService.create(1L, bookingCreateDto));
        Assertions.assertEquals("Вещь нельзя забронировать", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createNotAvailableItemForOwnerTest() {
        userService.create(user1);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));

        ItemNotFoundException thrown = Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.create(1L, bookingCreateDto));
        Assertions.assertEquals("Вещь нельзя забронировать у себя", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTest(){
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        BookingDto stored = bookingService.create(2L, bookingCreateDto);

        assertEquals(2L, stored.getBooker().getId());
        assertEquals("itemName", stored.getItem().getName());
        assertEquals(bookingCreateDto.getStart(), stored.getStart());
        assertEquals(bookingCreateDto.getEnd(), stored.getEnd());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getBookingByWrongIdTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.getBookingById(1L, 1L));
        Assertions.assertEquals("Бронирования c id1 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getBookingByWrongIdUserTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingById(1L, 3L));
        Assertions.assertEquals("Нет пользователя с доступом к информации", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getBookingByIdTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        BookingDto stored = bookingService.getBookingById(1L, 2L);

        assertEquals(2L, stored.getBooker().getId());
        assertEquals("itemName", stored.getItem().getName());
        assertEquals(bookingCreateDto.getStart(), stored.getStart());
        assertEquals(bookingCreateDto.getEnd(), stored.getEnd());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateApprovingWrongIdTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.updateApproving(1L, 1L, true));
        Assertions.assertEquals("Бронирования c id1 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateApprovingByWrongIdUserTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.updateApproving(1L, 3L, true));
        Assertions.assertEquals("Нет пользователя с доступом к изменению информации статуса бронирования", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateApprovingTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        BookingDto stored = bookingService.updateApproving(1L, 1L, true);

        assertEquals(2L, stored.getBooker().getId());
        assertEquals("itemName", stored.getItem().getName());
        assertEquals(bookingCreateDto.getStart(), stored.getStart());
        assertEquals(bookingCreateDto.getEnd(), stored.getEnd());
        assertEquals(Booking.BookingStatus.APPROVED, stored.getStatus());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateApprovingAgainTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        bookingService.updateApproving(1L, 1L, true);

        ValidationFailedException thrown = Assertions.assertThrows(ValidationFailedException.class,
                () -> bookingService.updateApproving(1L, 1L, true));
        Assertions.assertEquals("Статус бронирования не WAITING - обновление невозможно ", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateApprovingRejectedTest() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        BookingDto stored = bookingService.updateApproving(1L, 1L, false);

        assertEquals(2L, stored.getBooker().getId());
        assertEquals("itemName", stored.getItem().getName());
        assertEquals(bookingCreateDto.getStart(), stored.getStart());
        assertEquals(bookingCreateDto.getEnd(), stored.getEnd());
        assertEquals(Booking.BookingStatus.REJECTED, stored.getStatus());
    }
    @Test
    void getAll() {
    }

    @Test
    void getAllOwnersBooking() {
    }
}