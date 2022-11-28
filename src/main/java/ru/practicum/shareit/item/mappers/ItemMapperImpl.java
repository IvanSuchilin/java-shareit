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
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
        item.getOwner(), item.getRequest());
        return itemDto;
    }

    @Override
    public Item fromDTO(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                itemDto.getOwner(),itemDto.getRequest());
    }
}
