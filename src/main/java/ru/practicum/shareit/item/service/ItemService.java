package ru.practicum.shareit.item.service;

import exceptions.itemExceptions.InvalidItemDtoException;
import exceptions.userExceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

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
        if (itemDto.getAvailable() == null || itemDto.getDescription() == null
                || itemDto.getName().isEmpty()) {
            throw new InvalidItemDtoException("Отсутствуют необходимые данные для создания item");
        }
        Item itemFromDto = itemMapper.fromDTO(itemDto);
        User owner = userStorage.getUserById(userId);
        itemFromDto.setOwner(owner);
        return itemMapper.toDTO(itemStorage.create(itemFromDto));
    }

    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        if (!itemStorage.getItemById(itemId).getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("Нет такого владельца вещи");
        }
        Item itemFromDto = itemMapper.fromDTO(itemDto);
        User owner = userStorage.getUserById(userId);
        //itemFromDto.setOwner(owner);
        Item itemFromMemoryDto = itemMapper.fromDTO(getItemById(itemId));
       /* if (itemFromDto.getRequest() != null || itemFromDto.getOwner() != null || itemFromDto.getId() != null)*/
        if (itemFromDto.getRequest() != null || itemFromDto.getOwner() != null) {
            throw new RuntimeException("Нельзя менять владельца и id");
        }
        if (itemFromDto.getAvailable() != null) {
            itemFromMemoryDto.setAvailable(itemFromDto.getAvailable());
        }
        if (itemFromDto.getName() != null) {
            itemFromMemoryDto.setName(itemFromDto.getName());
        }
        if (itemFromDto.getDescription() != null) {
            itemFromMemoryDto.setDescription(itemFromDto.getDescription());
        }
        return itemMapper.toDTO(itemStorage.update(itemId, userId, itemFromMemoryDto));
    }

    public ItemDto getItemById(Long id) {
        log.debug("Получен запрос GET /items/{itemId}");
        List<Item> allItems = itemStorage.getAllItems();
        if (!allItems.stream().anyMatch(i -> i.getId() == id)) {
            throw new UserNotFoundException("Нет такого id");
        }
        return itemMapper.toDTO(itemStorage.getItemById(id));
    }

    public Collection<ItemDto> getAllUsersItems(Long userId) {
    }
}
