package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.user.dto.UserBookingDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemBookingDto item;
    private UserBookingDto booker;
    private Booking.BookingStatus status;
}
