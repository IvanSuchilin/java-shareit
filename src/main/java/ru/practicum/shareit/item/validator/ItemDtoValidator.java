package ru.practicum.shareit.item.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@Component
public class ItemDtoValidator {
    public void validateItemDto(ItemDto itemDto) {
        if (itemDto.getAvailable() == null || itemDto.getDescription() == null
                || itemDto.getName().isEmpty()) {
            throw new InvalidItemDtoException("Отсутствуют необходимые данные для создания item");
        }
    }
}
