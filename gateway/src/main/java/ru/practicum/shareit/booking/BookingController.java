package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    public static final String REQUEST_HEADER_SHARER = "X-Sharer-User-Id";
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(REQUEST_HEADER_SHARER) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("ПОлучение всех бронирований со статусом {}, userId={}, from={}, size={} в gateway", stateParam, userId, from, size);
        return bookingClient.getAllBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(REQUEST_HEADER_SHARER) long userId,
                                             @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Создание бронирования {}, userId={} в gateway", requestDto, userId);
        if (!requestDto.getStart().isBefore(requestDto.getEnd())) {
            throw new IllegalArgumentException("Дата старта не может быть в прошлом или равна дате окончания");
        }
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(REQUEST_HEADER_SHARER) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получение бронирования {}, userId={} в gateway", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllToOwner(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Получение бронирований владельцем {}, userId={}, from={}, size={} в gateway", stateParam, userId, from, size);
        return bookingClient.getAllOwnerBookings(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable("bookingId") Long bookingId,
                             @RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                             @RequestParam Boolean approved) {
        log.info("Подтверждение/отклонение бронирования вещи {} в gateway", bookingId);
        return bookingClient.updateApprovingBooking(bookingId, userId, approved);
    }
}
