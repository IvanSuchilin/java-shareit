package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemCreatingDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mappers.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toDTO(Item item);

    Item toItem(ItemCreatingDto itemDto);

    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemCreatingDto toCreatingDTO(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateItem(ItemDto itemDto, @MappingTarget Item item);

    @Mapping(target = "id", ignore = true)
    Comment toComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);
}

