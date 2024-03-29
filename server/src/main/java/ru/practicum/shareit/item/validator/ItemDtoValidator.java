package ru.practicum.shareit.item.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.item.ItemCreatingDto;

@Slf4j
@Component
public class ItemDtoValidator {
    public void validateItemDto(ItemCreatingDto itemDto) {
        if (itemDto.getAvailable() == null || itemDto.getDescription() == null
                || itemDto.getName().isEmpty()) {
            log.warn("Отсутствуют необходимые данные для создания item");
            throw new InvalidItemDtoException("Отсутствуют необходимые данные для создания item");
        }
    }
}
