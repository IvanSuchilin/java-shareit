package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BookingExceptions.InvalidBookingDtoException;
import ru.practicum.shareit.exceptions.BookingExceptions.ValidationFailedException;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.exceptions.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.ItemCreatingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/test/java/resources/scripts/schemaTest.sql"})
class BookingServiceIntTest {
    private static User user1;
    private static User user2;
    private static BookingCreateDto bookingCreateDto;

    private Sort sort = Sort.by(Sort.Direction.DESC, "start");
    private final Pageable pageable = PageRequest.of(0, 10, sort);
    private final EntityManager em;
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
        LocalDateTime startBooking = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(10).truncatedTo(ChronoUnit.SECONDS);
        bookingCreateDto = new BookingCreateDto(1L, startBooking, endBooking, user1);
    }

    @Test
    void createWrongUserTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.create(10L, bookingCreateDto));
        Assertions.assertEquals("Пользователя c id10 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void createWrongCreatingBookingDtoTest() {
        InvalidBookingDtoException thrown = Assertions.assertThrows(InvalidBookingDtoException.class,
                () -> bookingService.create(1L, new BookingCreateDto()));
        Assertions.assertEquals("Отсутствуют необходимые данные для создания Booking", thrown.getMessage());
    }


    @Test
    void createWrongItemTest() {
        userService.create(user1);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.create(1L, bookingCreateDto));
        Assertions.assertEquals("Предмета c id1 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void createNotAvailableItemTest() {
        userService.create(user1);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", false, null));

        InvalidItemDtoException thrown = Assertions.assertThrows(InvalidItemDtoException.class,
                () -> bookingService.create(1L, bookingCreateDto));
        Assertions.assertEquals("Вещь нельзя забронировать", thrown.getMessage());
    }

    @Test
    void createNotAvailableItemForOwnerTest() {
        userService.create(user1);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));

        ItemNotFoundException thrown = Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.create(1L, bookingCreateDto));
        Assertions.assertEquals("Вещь нельзя забронировать у себя", thrown.getMessage());
    }

    @Test
    void createTest() {
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
    void getBookingByWrongIdTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.getBookingById(1L, 1L));
        Assertions.assertEquals("Бронирования c id1 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
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
    void updateApprovingWrongIdTest() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.updateApproving(1L, 1L, true));
        Assertions.assertEquals("Бронирования c id1 нет", thrown.getReason());
        Assertions.assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
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
    void getAllWithAll() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);

        List<BookingDto> bookings = bookingService.getAll(2L, "ALL", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void getAllWithRejected() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        bookingService.updateApproving(1L, 1L, false);

        List<BookingDto> bookings = bookingService.getAll(2L, "REJECTED", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void getAllWithWaiting() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);

        List<BookingDto> bookings = bookingService.getAll(2L, "WAITING", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllWithCurrent() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<BookingDto> bookings = bookingService.getAll(2L, "CURRENT", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllWithPast() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(10));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<BookingDto> bookings = bookingService.getAll(2L, "PAST", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllWithFuture() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(12));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<BookingDto> bookings = bookingService.getAll(2L, "FUTURE", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllWithUnsupported() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(12));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        ValidationFailedException thrown = Assertions.assertThrows(ValidationFailedException.class,
                () -> bookingService.getAll(2L, "status", pageable));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", thrown.getMessage());
    }

    @Test
    void getAllWithWrongId() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.getAll(2L, "status", null));
        Assertions.assertEquals("Пользователя c id2 нет", thrown.getReason());
    }

    @Test
    void getAllOwnerWithWrongId() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookingService.getAllOwnersBooking(2L, "status", null));
        Assertions.assertEquals("Пользователя c id2 нет", thrown.getReason());
    }

    @Test
    void getAllOwnerWithAll() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);

        List<BookingDto> bookings = bookingService.getAllOwnersBooking(1L, "ALL", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void getAllOwnerWithRejected() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);
        bookingService.updateApproving(1L, 1L, false);

        List<BookingDto> bookings = bookingService.getAllOwnersBooking(1L, "REJECTED", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void getAllOwnerWithWaiting() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(1L, new ItemCreatingDto(null, "itemName",
                "description", true, null));
        bookingService.create(2L, bookingCreateDto);

        List<BookingDto> bookings = bookingService.getAllOwnersBooking(1L, "WAITING", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllOwnerWithCurrent() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<BookingDto> bookings = bookingService.getAllOwnersBooking(1L, "CURRENT", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllOwnerWithPast() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(10));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<BookingDto> bookings = bookingService.getAllOwnersBooking(1L, "PAST", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllOwnerWithFuture() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(12));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<BookingDto> bookings = bookingService.getAllOwnersBooking(1L, "FUTURE", pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    @Transactional
    void getAllOwnerWithUnsupported() {
        userService.create(user1);
        userService.create(user2);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(12));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        ValidationFailedException thrown = Assertions.assertThrows(ValidationFailedException.class,
                () -> bookingService.getAllOwnersBooking(1L, "status", pageable));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", thrown.getMessage());
    }

    @Test
    void getAllOwnerWithoutBookings() {
        userService.create(user1);
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllOwnersBooking(1L, "status", null));
        Assertions.assertEquals("У пользователя нет вещей для аренды", thrown.getMessage());
    }

}