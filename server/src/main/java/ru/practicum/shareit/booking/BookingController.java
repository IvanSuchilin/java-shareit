package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class BookingController {
    private final UserService userService;
    private final BookingService bookingService;
    public static final String REQUEST_HEADER_SHARER = "X-Sharer-User-Id";

    @PostMapping("/bookings")
    public BookingDto create(@RequestHeader(REQUEST_HEADER_SHARER) Long userId, @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("Создание бронирования вещи id {}", bookingCreateDto.getItemId());
        userService.getUserById(userId);
        return bookingService.create(userId, bookingCreateDto);
    }

    @GetMapping("/bookings/{bookingId}")
    public BookingDto get(@PathVariable("bookingId") Long id, @RequestHeader(REQUEST_HEADER_SHARER) Long userId) {
        log.info("Получение информации о бронировании id {}", id);
        return bookingService.getBookingById(id, userId);
    }

    @PatchMapping("/bookings/{bookingId}")
    public BookingDto update(@PathVariable("bookingId") Long bookingId,
                             @RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                             @RequestParam Boolean approved) {
        log.info("Подтверждение/отклонение бронирования вещи {}", bookingId);
        bookingService.getBookingById(bookingId, userId);
        return bookingService.updateApproving(bookingId, userId, approved);
    }

    @GetMapping("/bookings")
    public List<BookingDto> getAll(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                   @RequestParam(required = false) String state,
                                   @RequestParam(defaultValue = "0", required = false) int from,
                                   @RequestParam(defaultValue = "20", required = false) int size) {
        userService.getUserById(userId);
        return bookingService.getAll(userId, state, createPageable(from, size));
    }

    @GetMapping("/bookings/owner")
    public List<BookingDto> getAllToOwner(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                          @RequestParam(required = false) String state,
                                          @RequestParam(defaultValue = "0", required = false) int from,
                                          @RequestParam(defaultValue = "20", required = false) int size) {
        userService.getUserById(userId);
        return bookingService.getAllOwnersBooking(userId, state, createPageable(from, size));
    }

    private Pageable createPageable(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        return PageRequest.of((from / size), size, sort);
    }
}
