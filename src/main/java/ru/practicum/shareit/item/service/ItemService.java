package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
        //private final ItemValidator itemValidator;
        //private final ItemDtoValidator itemDtoValidator;
        private final ItemStorage itemStorage;
        private final ItemMapper itemMapper;
        private final UserStorage userStorage;

        public ItemDto create(Long userId, ItemDto itemDto) {
                log.debug("Получен запрос POST /items");
                Item itemFromDto = itemMapper.fromDTO(itemDto);
                User owner = userStorage.getUserById(userId);
                itemFromDto.setOwner(owner);
                return itemMapper.toDTO(itemStorage.create(itemFromDto));
        }
}
