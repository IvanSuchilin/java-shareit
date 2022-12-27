package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

import static ru.practicum.shareit.item.constants.RequestConstants.REQUEST_HEADER_SHARER;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping
public class ItemRequestController {
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(UserService userService, BookingService bookingService, ItemService itemService,
                                 ItemRequestService itemRequestService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.itemRequestService = itemRequestService;
    }

    @PostMapping("/requests")
    public ItemRequestDto create(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                 @RequestBody @Valid ItemRequestCreatingDto itemRequestCreatingDto
    ) {
        log.info("Создание запроса вещи c с описанием {}", itemRequestCreatingDto.getDescription());
        userService.getUserById(userId);
        return itemRequestService.create(userId, itemRequestCreatingDto);
    }
}
