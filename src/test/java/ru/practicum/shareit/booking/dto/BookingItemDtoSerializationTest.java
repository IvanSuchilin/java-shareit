package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.user.dto.UserBookingDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingItemDtoSerializationTest {
    @Autowired
    private JacksonTester<BookingItemDto> jacksonTesterBookingItemDto;
    @Autowired
    private JacksonTester<BookingDto> jacksonTesterBookingDto;

    @SneakyThrows
    @Test
    void bookingItemDtoSerializationTest() {
        BookingItemDto bookingItemDto = new BookingItemDto(1L, 1L);
        JsonContent<BookingItemDto> json = jacksonTesterBookingItemDto.write(bookingItemDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void bookingDtoSerializationTest() {
        UserBookingDto user = new UserBookingDto(1L, "userName");
        ItemBookingDto item = new ItemBookingDto(1L, "itemName");
        LocalDateTime startBooking = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(10).truncatedTo(ChronoUnit.SECONDS);
       BookingDto bookingDto = new BookingDto(1L, startBooking, endBooking, item, user, Booking.BookingStatus.APPROVED);
        JsonContent<BookingDto> json = jacksonTesterBookingDto.write(bookingDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.booker.name").isEqualTo("userName");
        assertThat(json).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
    }
}