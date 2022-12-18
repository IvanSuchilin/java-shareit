package ru.practicum.shareit.booking.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.exceptions.BookingExceptions.InvalidBookingDtoException;

import java.time.LocalDateTime;

@Slf4j
@Component
public class BookingValidator {
    public void validateBookingCreateDto(BookingCreateDto bookingCreateDto) {
        if (bookingCreateDto.getItemId() == null ||
                bookingCreateDto.getStart().isBefore(LocalDateTime.now()) ||
                bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart()) ||
                bookingCreateDto.getEnd().isBefore(LocalDateTime.now())) {
            log.warn("Отсутствуют корректные данные для создания Booking");
            throw new InvalidBookingDtoException("Отсутствуют необходимые данные для создания Booking");
        }
}
}