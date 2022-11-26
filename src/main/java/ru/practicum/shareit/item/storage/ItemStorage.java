package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    List<Item> getAllItems();

    ItemDto createItem(ItemDto itemDto);

    ItemDto update(Long id, ItemDto itemDto);

    User getItemById(Long itemId);
}
