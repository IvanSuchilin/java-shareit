package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping
public class RequestController {

    private final RequestClient requestClient;
    public static final String REQUEST_HEADER_SHARER = "X-Sharer-User-Id";

    @PostMapping("/requests")
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                         @RequestBody @Valid ItemRequestCreatingDto itemRequestCreatingDto
    ) {
        log.info("Создание запроса вещи c с описанием {}", itemRequestCreatingDto.getDescription());
        return requestClient.createItemRequest(userId, itemRequestCreatingDto);
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getUsersRequests(@RequestHeader(REQUEST_HEADER_SHARER) Long userId) {
        log.info("Получение всех запросов пользователя id {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                             @PathVariable("requestId") Long id) {
        log.info("Получение данных запроса  id {}", id);
        return requestClient.getRequestById(userId, id);
    }

    @GetMapping("/requests/all")
    public ResponseEntity<Object> getAllRequestsWithPagination(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                                 @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                                                 @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        log.info("Получение всех запросов  c пагинацией");
        return requestClient.getAll(userId, from, size);
    }
}
