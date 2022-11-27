package ru.practicum.shareit.item.storage;

import exceptions.itemExceptions.ItemNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
@Getter
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private Long idMemory = 0L;

    private Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public List<Item> getAllOwnerItems(Long id) {
        return items.get(id);
    }

    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<>();
        for (Long k : items.keySet()) {
           /* allItems = Stream.of(items.get(k)).flatMap(List::stream).collect(Collectors.toList());*/
            for (Item i: items.get(k)) {
                allItems.add(i);
            }
        }
        return allItems;
    }

    @Override
    public Item create(Item item) {
        idMemory++;
            item.setId(idMemory);
           /* items.put(item.getId(), item);
            return items.get(item.getId());*/
        items.compute(item.getOwner().getId(), (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
        }

    @Override
    public ItemDto update(Long id, ItemDto itemDto) {
        return null;
    }

    @Override
    public Item getItemById(Long itemId) {
        List<Item> allItems = getAllItems();
       return allItems.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
