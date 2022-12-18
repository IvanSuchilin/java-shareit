package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingValidator bookingValidator;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public Booking create(Long userId, BookingCreateDto bookingCreateDto) {
        bookingValidator.validateBookingCreateDto(bookingCreateDto);
        //log.info("Создание бронирования вещи id {}", bookingCreateDto.getItem().getId());
        if (userRepository.findAll()
                .stream()
                .noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new UserNotFoundException("Нет такого id");
        }
        User booker = userRepository.getReferenceById(userId);
        Long id = bookingCreateDto.getItemId();
        Item item = itemRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Предмета c id" + id + " нет"));
        if (!item.getAvailable()){
            throw new InvalidItemDtoException("Вещь нельзя забронировать");
        }
        //Booking bookingForSave = BookingMapper.INSTANCE.toBooking(bookingCreateDto);
        Booking newBooking = new Booking();
        newBooking.setStart(bookingCreateDto.getStart());
        newBooking.setEnd(bookingCreateDto.getEnd());
        newBooking.setBooker(booker);
        newBooking.setItem(item);
        newBooking.setStatus(Booking.BookingStatus.WAITING);
        //Item itemFromDto = ItemMapper.INSTANCE.toItem(itemDto, owner.getId(),owner.getName(), owner.getEmail());
       // Item itemFromDto = ItemMapper.INSTANCE.toItem(itemDto);
       // itemFromDto.setOwner(owner);
        //return ItemMapper.INSTANCE.toDTO(bookingRepository.save(itemFromDto));
//BookingDto bookingDto = BookingMapper.INSTANCE.toBookingDto(bookingForSave);

        return bookingRepository.save(newBooking);
        }
}
