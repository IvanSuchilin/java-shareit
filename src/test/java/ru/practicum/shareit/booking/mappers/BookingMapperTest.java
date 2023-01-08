package ru.practicum.shareit.booking.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class BookingMapperTest {
    private Booking booking;

    @BeforeEach
    void setup() {
        User user = new User(null, "name1", "emailtest1@mail.ru");
        Item item = new Item(1L, "itemName", "description", true, user, null, null);
        LocalDateTime startBooking = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(10).truncatedTo(ChronoUnit.SECONDS);
        booking = new Booking(1L, startBooking, endBooking, item, user, Booking.BookingStatus.APPROVED);
    }

    @Test
    void toBookingDto() {
        BookingDto bookingDto = BookingMapper.INSTANCE.toBookingDto(booking);

        Assertions.assertEquals(bookingDto.getEnd(), booking.getEnd());
        Assertions.assertEquals(bookingDto.getStart(), booking.getStart());
        Assertions.assertEquals(bookingDto.getItem().getName(), booking.getItem().getName());
        Assertions.assertEquals(bookingDto.getBooker().getName(), booking.getBooker().getName());
    }

    @Test
    void toBookingItemDto() {
        BookingItemDto bookingItemDto = BookingMapper.INSTANCE.toBookingItemDto(booking);

        Assertions.assertEquals(bookingItemDto.getBookerId(), booking.getBooker().getId());
    }
}