package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exceptions.BookingExceptions.ValidationFailedException;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.exceptions.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingValidator bookingValidator;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public BookingDto create(Long userId, BookingCreateDto bookingCreateDto) {
        bookingValidator.validateBookingCreateDto(bookingCreateDto);
        log.info("Создание бронирования вещи {}", bookingCreateDto.getItemId());
        /*if (userRepository.findAll()
                .stream()
                .noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new UserNotFoundException("Нет такого id пользователя");
        }*/
        User booker = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Пользователя c id" + userId + " нет"));
        Long id = bookingCreateDto.getItemId();
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Предмета c id" + id + " нет"));
        if (!item.getAvailable()) {
            throw new InvalidItemDtoException("Вещь нельзя забронировать");
        }
        if(item.getOwner().getId().equals(userId)){
            throw new ItemNotFoundException("Вещь нельзя забронировать у себя");
        }
        Booking newBooking = new Booking();
        newBooking.setStart(bookingCreateDto.getStart());
        newBooking.setEnd(bookingCreateDto.getEnd());
        newBooking.setBooker(booker);
        newBooking.setItem(item);
        newBooking.setStatus(Booking.BookingStatus.WAITING);
        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(newBooking));
    }

    public BookingDto getBookingById(Long id, Long userId) {
        bookingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Бронирования c id" + id + " нет"));
        Booking storedBooking = bookingRepository.findById(id).get();
        if (!Objects.equals(userId, storedBooking.getBooker().getId()) &&
                !Objects.equals(userId, storedBooking.getItem().getOwner().getId())) {
            throw new UserNotFoundException("Нет пользователя с доступом к информации");
        }
        return BookingMapper.INSTANCE.toBookingDto(storedBooking);
    }

    public BookingDto updateApproving(Long bookingId, Long userId, Boolean approved) {
        Booking storedBooking = bookingRepository.findById(bookingId).get();
        if (!Objects.equals(storedBooking.getItem().getOwner().getId(), userId)) {
            throw new UserNotFoundException("Нет пользователя с доступом к изменению информации статуса бронирования");
        }
        if (!Objects.equals(Booking.BookingStatus.WAITING, storedBooking.getStatus())) {
            throw new ValidationFailedException("Статус бронирования не WAITING - обновление невозможно ");
        }
        if (approved) {
            storedBooking.setStatus(Booking.BookingStatus.APPROVED);
        } else {
            storedBooking.setStatus(Booking.BookingStatus.REJECTED);
        }
        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(storedBooking));
    }

    public List<BookingDto> getAll(Long userId, String state) {
        List<Booking> bookings;
        User userStored = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Пользователя c id" + userId + " нет"));
        Booking.BookingState bookingState = Objects.isNull(state) ?
                Booking.BookingState.ALL : Booking.BookingState.getBookingStateFromQuery(state);
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerOrderByStartDesc(userStored);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentByBooker(userStored, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findPastByBooker(userStored,LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureByBooker(userStored, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userStored,Booking.BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userStored,Booking.BookingStatus.REJECTED);
                break;
            default:
                throw new  ValidationFailedException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper.INSTANCE::toBookingDto).
                collect(Collectors.toList());
    }

    public List<BookingDto> getAllOwnersBooking(Long userId, String state) {
        List<Booking> bookings;
        User ownerStored = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Пользователя c id" + userId + " нет"));
        if (itemRepository.findItemByOwnerId(userId).size() == 0){
            throw new UserNotFoundException("У пользователя нет вещей для аренды");
        }
        Booking.BookingState bookingState = Objects.isNull(state) ?
                Booking.BookingState.ALL : Booking.BookingState.getBookingStateFromQuery(state);
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerItems(ownerStored);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentByOwnerItems(ownerStored, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findPastByOwnerItems(ownerStored,LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureByOwnerItems(ownerStored, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerAndAndStatusOrderByStart(ownerStored,Booking.BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerAndAndStatusOrderByStart(ownerStored,Booking.BookingStatus.REJECTED);
                break;
            default:
                throw new  ValidationFailedException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper.INSTANCE::toBookingDto).
                collect(Collectors.toList());
    }
}
