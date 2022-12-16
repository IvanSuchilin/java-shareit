package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemDtoValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemDtoValidator itemDtoValidator;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    //private final ItemStorage itemStorage;
    //private final ItemMapper itemMapper;
    //private final UserStorage userStorage;

    public ItemDto create(Long userId, ItemDto itemDto) {
        itemDtoValidator.validateItemDto(itemDto);
        log.debug("Получен на создание вещи {}", itemDto.getName());
        User owner = userRepository.getReferenceById(userId);
        //Item itemFromDto = ItemMapper.INSTANCE.toItem(itemDto, owner.getId(),owner.getName(), owner.getEmail());
        Item itemFromDto = ItemMapper.INSTANCE.toItem(itemDto);
       itemFromDto.setOwner(owner);
        // return ItemMapper.INSTANCE.toDTO(itemRepository.save(itemFromDto));
        // ItemMapper.INSTANCE.toDTO(itemRepository.save(itemFromDto));
      //  Optional<Item> finItem =  itemRepository.findById(1L);
        return ItemMapper.INSTANCE.toDTO(itemRepository.save(itemFromDto));
    }

    public ItemDto getItemById(Long id) {
        log.debug("Получен запрос GET /items/{itemId}");
        List<Item> allItems = itemRepository.findAll();
        if (allItems.stream().noneMatch(i -> Objects.equals(i.getId(), id))) {
            throw new UserNotFoundException("Нет такого id");
        }
        ItemDto finItem =  ItemMapper.INSTANCE.toDTO(itemRepository.getReferenceById(id));

        return ItemMapper.INSTANCE.toDTO(itemRepository.getReferenceById(id));
    }

    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        if (!itemRepository.getReferenceById(itemId).getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("Нет такого владельца вещи");
        }
        //Item itemFromDto = ItemMapper.INSTANCE.toItem(itemDto);
       // Item itemFromMemoryDto = ItemMapper.INSTANCE.toItem(getItemById(itemId));
       /* if (itemFromDto.getId() != null || itemFromDto.getOwner() != null) {
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
        }*/
        try {
            Item stored = itemRepository.findById(itemId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            ItemMapper.INSTANCE.updateItem(itemDto, stored);
            return ItemMapper.INSTANCE.toDTO(itemRepository.save(stored));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new UserNotFoundException("Нет такой вещи");
        }
    }

   /*



    public Collection<ItemDto> getAllUsersItems(Long userId) {
        log.debug("Получен запрос GET /items");
        return itemStorage.getAllOwnerItems(userId)
                .stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text)
                .stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }*/
}
