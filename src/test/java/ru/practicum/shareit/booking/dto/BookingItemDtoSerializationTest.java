package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingItemDtoSerializationTest {
    @Autowired
    private JacksonTester<BookingItemDto> jacksonTesterBookingItemDto;

    @SneakyThrows
    @Test
    void bookingItemDtoSerializationTest() {
        BookingItemDto bookingItemDto = new BookingItemDto(1L, 1L);
        JsonContent<BookingItemDto> json = jacksonTesterBookingItemDto.write(bookingItemDto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}