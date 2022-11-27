package ru.practicum.shareit.item.storage;

import exceptions.itemExceptions.ItemNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Item update(Long itemId,Long ownerId, Item item) {
        List<Item> ownersItems = items.get(ownerId);
        for (int i = 0; i < ownersItems.size(); i++){
            if (ownersItems.get(i).getId() == itemId){
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
        List<Item> allItems = getAllItems();
        List<Item> saerchInName = allItems.stream().filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
        List<Item> saerchInDescription = allItems.stream().filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
        return Stream.of(saerchInName, saerchInDescription).distinct().flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
