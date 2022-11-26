package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {
    ItemDto toDTO(Item item);
}
