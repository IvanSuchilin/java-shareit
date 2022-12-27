package ru.practicum.shareit.request.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mappers.UserMapper;

@Mapper(componentModel = "spring",
        uses = {
                UserMapper.class,
                ItemMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);
    //@Mapper(target = "", source = "")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);
}
