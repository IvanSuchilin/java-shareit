package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingExceptions.ValidationFailedException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemCreatingDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemDtoValidator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemDtoValidator itemDtoValidator;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    private final CommentRepository commentRepository;

    public ItemCreatingDto create(Long userId, ItemCreatingDto itemDto) {
        itemDtoValidator.validateItemDto(itemDto);
        log.debug("Получен на создание вещи {}", itemDto.getName());
        if (userRepository.findAll()
                .stream()
                .noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new UserNotFoundException("Нет такого id");
        }
        User owner = userRepository.getReferenceById(userId);
        Item itemFromDto = ItemMapper.INSTANCE.toItem(itemDto);
        itemFromDto.setOwner(owner);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Запроса c id" + itemDto.getRequestId() + " нет"));
            itemFromDto.setItemRequest(itemRequest);
        }
        return ItemMapper.INSTANCE.toCreatingDTO(itemRepository.save(itemFromDto));
    }

    public ItemDto getItemById(Long id, Long userId) {
        log.debug("Получен запрос GET /items/{itemId}");
        Item itemWithoutBooking = itemRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Предмета c id" + id + " нет"));
        if (!itemWithoutBooking.getOwner().getId().equals(userId)) {
            return ItemMapper.INSTANCE.toDTO(itemWithoutBooking);
        } else {
            return createItemDtoWithBooking(itemWithoutBooking);
        }
    }

    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        Item stored = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Предмета c id" + itemId + " нет"));
        if (!itemRepository.findById(itemId).get().getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("Нет такого владельца вещи");
        }
        ItemMapper.INSTANCE.updateItem(itemDto, stored);
        return ItemMapper.INSTANCE.toDTO(itemRepository.save(stored));
    }

    public Collection<ItemDto> getAllUsersItems(Long userId, Pageable pageable) {
        log.debug("Получен запрос GET /items");
        return itemRepository.findItemByOwnerIdOrderById(userId, pageable)
                .stream()
                .map(this::createItemDtoWithBooking)
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItem(String text, Pageable pageable) {
        log.debug("Получен запрос GET /items/search. Найти вещь по запросу {} ", text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        String lowText = text.toLowerCase();
        return itemRepository.findByText(lowText, pageable)
                .stream()
                .map(ItemMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }


    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new ValidationFailedException("Комментарий не может быть пустым");
        }
        User author = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Предмета c id" + itemId + " нет"));
        if (!bookingRepository.existsByItemAndAndBookerAndEndBefore(item, author, LocalDateTime.now())) {
            throw new ValidationFailedException("Бронирование пользователя не завершено или не существует");
        }
        Comment newComment = ItemMapper.INSTANCE.toComment(commentDto);
        newComment.setItem(item);
        newComment.setAuthor(author);
        newComment.setCreated(LocalDateTime.now());
        return ItemMapper.INSTANCE.toCommentDto(commentRepository.save(newComment));
    }

    private ItemDto createItemDtoWithBooking(Item itemWithoutBooking) {
        LocalDateTime start = LocalDateTime.now();
        ItemDto itemDtoWithoutBooking = ItemMapper.INSTANCE.toDTO(itemWithoutBooking);
        Booking nextBooking = bookingRepository.findFirstByItemAndStartAfterOrderByStartDesc(itemWithoutBooking, start);
        BookingItemDto nextBookingItemDto = BookingMapper.INSTANCE.toBookingItemDto(nextBooking);
        Booking lastBooking = bookingRepository.findFirstByItemAndStartBeforeOrderByStartDesc(itemWithoutBooking, start);
        BookingItemDto lastBookingItemDto = BookingMapper.INSTANCE.toBookingItemDto(lastBooking);
        itemDtoWithoutBooking.setLastBooking(lastBookingItemDto);
        itemDtoWithoutBooking.setNextBooking(nextBookingItemDto);
        return itemDtoWithoutBooking;
    }
}
