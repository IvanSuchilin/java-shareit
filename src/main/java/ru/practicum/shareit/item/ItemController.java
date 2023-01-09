package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    public static final String REQUEST_HEADER_SHARER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("/items")
    public ItemCreatingDto create(@RequestHeader(REQUEST_HEADER_SHARER) Long userId, @RequestBody ItemCreatingDto itemDto) {
        log.info("Создание вещи {}", itemDto.getName());
        userService.getUserById(userId);
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/items/{itemId}")
    public ItemDto get(@PathVariable("itemId") Long id, @RequestHeader(REQUEST_HEADER_SHARER) Long userId) {
        log.info("Получение информации о вещи id {} пользователем {}", id, userId);
        userService.getUserById(userId);
        return itemService.getItemById(id, userId);
    }

    @PatchMapping("/items/{itemId}")
    public ItemDto patch(@PathVariable("itemId") Long id, @RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                         @RequestBody ItemDto itemDto) {
        log.info("Обновление данных вещи {}", id);
        userService.getUserById(userId);
        itemService.getItemById(id, userId);
        return itemService.update(id, userId, itemDto);
    }

    @GetMapping("/items")
    public Collection<ItemDto> findAllUsersItems(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                                 @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        log.info("Получение всех вещей пользователя");
        userService.getUserById(userId);
        final Pageable pageable = PageRequest.of((from / size), size);
        return itemService.getAllUsersItems(userId, pageable);
    }

    @GetMapping("/items/search")
    public Collection<ItemDto> searchItem(@RequestParam String text,
                                          @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                          @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        log.debug("Получен запрос GET /items/search. Найти вещь по запросу {} ", text);
        final Pageable pageable = PageRequest.of((from / size), size);
        return itemService.searchItem(text, pageable);
    }

    @PostMapping("/items/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(REQUEST_HEADER_SHARER) Long userId,
                                    @PathVariable("itemId") Long itemId, @RequestBody CommentDto commentDto) {
        log.info("Создание комментария к вещи id {} пользователем {}", itemId, userId);
        userService.getUserById(userId);
        return itemService.createComment(userId, itemId, commentDto);
    }
}
