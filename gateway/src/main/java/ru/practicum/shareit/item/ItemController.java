package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping
public class ItemController {
    private final ItemClient itemClient;
    public static final String REQUEST_HEADER_SHARER = "X-Sharer-User-Id";


    @PostMapping("/items")
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER_SHARER) Long userId, @RequestBody @Valid ItemCreatingDto itemDto) {
        log.info("Создание вещи в getway {}", itemDto.getName());
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Object> get(@PathVariable("itemId") Long id, @RequestHeader(REQUEST_HEADER_SHARER) Long userId) {
        log.info("Получение информации о вещи id {} пользователем {} в getway", id, userId);
        return itemClient.getItemById(id, userId);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Object> patch(@PathVariable("itemId") Long id, @RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                        @RequestBody ItemDto itemDto) {
        log.info("Обновление данных вещи {} в getway", id);
        return itemClient.updateItem(userId, id, itemDto);
    }

    @GetMapping("/items")
    public ResponseEntity<Object> findAllUsersItems(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                                    @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        log.info("Получение всех вещей пользователя в getway");
        return itemClient.getAllUserItems(from, size, userId);
    }

    @GetMapping("/items/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                             @RequestParam String text,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                             @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        log.debug("Получен запрос GET /items/search. Найти вещь по запросу {} в getway", text);
        return itemClient.searchItemByText(userId, text, from, size);
    }

    @PostMapping("/items/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                @PathVariable("itemId") Long itemId, @RequestBody CommentDto commentDto) {
        log.info("Создание комментария к вещи id {} пользователем {} в getway", itemId, userId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
