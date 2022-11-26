package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private Long idMemory = 0L;

    private Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getAllItems() {
        return null;
    }

    @Override
    public Item create(Item item) {
        idMemory++;
            item.setId(idMemory);
            items.put(item.getId(), item);
            return items.get(item.getId());
        }

    @Override
    public ItemDto update(Long id, ItemDto itemDto) {
        return null;
    }

    @Override
    public User getItemById(Long itemId) {
        return null;
    }
}
