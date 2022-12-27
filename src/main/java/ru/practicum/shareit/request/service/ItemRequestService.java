package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
  private final ItemRequestRepository itemRequestRepository;
    private final  UserRepository userRepository;

    public ItemRequestDto create(Long userId, ItemRequestCreatingDto itemRequestCreatingDto) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Пользователя c id" + userId + " нет"));
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestCreatingDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(requester);
        ItemRequestDto itemRequestDto = ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequestRepository.save(itemRequest));
        return itemRequestDto;
    }
}
