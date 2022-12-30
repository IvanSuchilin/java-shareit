package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestDto create(Long userId, ItemRequestCreatingDto itemRequestCreatingDto) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Пользователя c id" + userId + " нет"));
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestCreatingDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(requester);
        return ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<RequestResponseDto> getAllByUserId(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        return itemRequests.stream()
                .map(ItemRequestMapper.INSTANCE::toRequestResponseDto)
                .collect(Collectors.toList());
    }

    public RequestResponseDto getRequestById(Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Запроса c id" + id + " нет"));
        return ItemRequestMapper.INSTANCE.toRequestResponseDto(itemRequest);
    }
    public List<RequestResponseDto> getAllRequestsWithPagination(int from,int size, Long userId) {
            if (from == 0 && size == 0) {
                throw new BadRequestException("Не заданы параметры пагинации");
            }
            if (from < 0 || size < 0) {
                throw new BadRequestException("Неверно заданы параметры пагинации");
            }
            Pageable pageable = PageRequest.of(from, size);
            List<ItemRequest> requests = itemRequestRepository.findAllWithPagination(pageable, userId);
            return requests.stream()
                    .map(ItemRequestMapper.INSTANCE::toRequestResponseDto)
                    .collect(Collectors.toList());
    }
}
