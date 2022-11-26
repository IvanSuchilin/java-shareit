package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
        //private final ItemValidator itemValidator;
        //private final ItemDtoValidator itemDtoValidator;
        private final ItemStorage itemStorage;
}
