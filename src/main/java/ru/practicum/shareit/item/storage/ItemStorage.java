package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getAllOwnerItems(Long id);
   List<Item> getAllItems();

    Item create(Item item);

    ItemDto update(Long id, ItemDto itemDto);

    Item getItemById(Long itemId);
}
