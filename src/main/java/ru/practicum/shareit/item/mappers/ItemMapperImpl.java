package ru.practicum.shareit.item.mappers;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Service
public class ItemMapperImpl implements  ItemMapper{
    @Override
    public ItemDto toDTO(Item item) {
        if (item == null) {
            return null;
        }
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(),
        item.getOwner(), item.getRequest());
        return itemDto;
    }
}