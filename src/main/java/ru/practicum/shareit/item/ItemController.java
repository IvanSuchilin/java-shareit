package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, ItemMapper itemMapper, UserService userService) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.userService = userService;
    }

    @PostMapping("/items")
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.info("Создание вещи {}", itemDto.getName());
        userService.getUserById(userId);
        return itemService.create(userId, itemDto);
    }
}
