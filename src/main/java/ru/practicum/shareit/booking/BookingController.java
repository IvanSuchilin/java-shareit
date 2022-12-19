package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static ru.practicum.shareit.item.constants.RequestConstants.REQUEST_HEADER_SHARER;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping
public class BookingController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public BookingController(ItemService itemService, UserService userService, BookingService bookingService) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

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
                             @RequestParam(name = "approved") Boolean approved) {
        log.info("Подтверждение/отклонение бронирования вещи {}", bookingId);
        bookingService.getBookingById(bookingId, userId);
        return bookingService.updateApproving(bookingId, userId, approved);
    }

    @GetMapping("/bookings")
    public List<BookingDto> getAll(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                   @RequestParam(name = "state", required = false) String state) {
        userService.getUserById(userId);
        return bookingService.getAll(userId, state);
    }

}
