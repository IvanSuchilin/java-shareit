package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@Validated
@RequestMapping
public class ItemRequestController {
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    public static final String REQUEST_HEADER_SHARER = "X-Sharer-User-Id";

    @Autowired
    public ItemRequestController(UserService userService,
                                 ItemRequestService itemRequestService) {
        this.userService = userService;
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

    @GetMapping("/requests")
    public List<RequestResponseDto> getUsersRequests(@RequestHeader(REQUEST_HEADER_SHARER) Long userId) {
        log.info("Получение всех запросов пользователя id {}", userId);
        userService.getUserById(userId);
        return itemRequestService.getAllByUserId(userId);
    }

    @GetMapping("/requests/{requestId}")
        public RequestResponseDto getRequestById(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                 @PathVariable ("requestId") Long id){
            log.info("Получение данных запроса  id {}", id);
            userService.getUserById(userId);
            return itemRequestService.getRequestById(id);
        }

    @GetMapping("/requests/all")
    public List<RequestResponseDto> getAllRequestsWithPagination(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                   @PositiveOrZero @RequestParam (defaultValue = "0", required = false) int from,
                                                   @Positive @RequestParam (defaultValue = "20", required = false) int size){
        log.info("Получение всех запросов  c пагинацией");
        userService.getUserById(userId);
        return itemRequestService.getAllRequestsWithPagination(from, size, userId);
    }
}
