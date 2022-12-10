package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getAllOwnerItems(Long id);

    List<Item> getAllItems();

    Item create(Item item);

    Item update(Long itemId, Long ownerId, Item item);

    Item getItemById(Long itemId);

    List<Item> searchItem(String text);
}
