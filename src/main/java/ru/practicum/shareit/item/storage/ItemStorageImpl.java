package ru.practicum.shareit.item.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            allItems.addAll(items.get(k));
        }
        return allItems;
    }

    @Override
    public Item create(Item item) {
        idMemory++;
        item.setId(idMemory);
        items.compute(item.getOwner().getId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(Long itemId, Long ownerId, Item item) {
        List<Item> ownersItems = items.get(ownerId);
        for (int i = 0; i < ownersItems.size(); i++) {
            if (ownersItems.get(i).getId().equals(itemId)) {
                ownersItems.remove(i);
            }
        }
        items.get(ownerId).add(item);
        return getItemById(itemId);
    }

    @Override
    public Item getItemById(Long itemId) {
        List<Item> allItems = getAllItems();
        return allItems.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> allItems = getAllItems();
        List<Item> searchInName = allItems.stream().filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
        List<Item> searchInDescription = allItems.stream().filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
        List<Item> searching = Stream.of(searchInName, searchInDescription).distinct().flatMap(List::stream)
                .collect(Collectors.toList());
        return searching.stream().distinct().filter(item -> item.getAvailable().equals(true)).collect(Collectors.toList());
    }
}
