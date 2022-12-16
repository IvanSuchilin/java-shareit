package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mappers.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);
    ItemDto toDTO(Item item);
    Item toItem(ItemDto itemDto);

    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "owner.name", source = "userName")
    @Mapping(target = "owner.email", source = "userEmail")
    Item toItem(ItemDto itemDto, Long userId, String userName, String userEmail);
    //Item toItem(ItemDto itemDto, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateItem (ItemDto itemDto, @MappingTarget Item item);
}

