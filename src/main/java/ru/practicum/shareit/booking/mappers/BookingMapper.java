package ru.practicum.shareit.booking.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mappers.UserMapper;

@Mapper(componentModel = "spring",
        uses = {
                UserMapper.class,
                ItemMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper( BookingMapper.class);
   // @Mapping(target = "start", source = "start")
    //@Mapping(target = "end", source = "end")
    //@Mapping(target = "item.id", source = "itemId")
   /* @Mapping(target = "start", source = "start", qualifiedByName = "convertToLocalDateTime")
    @Mapping(target = "end", source = "end", qualifiedByName = "convertToLocalDateTime")*/
    Booking toBooking(BookingCreateDto bookingCreateDto);


    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "owner.name", source = "userName")
    @Mapping(target = "owner.email", source = "userEmail")
    Item toItem(ItemDto itemDto, Long userId, String userName, String userEmail);
    //Item toItem(ItemDto itemDto, User user);

    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
   /* @Mapping(target = "start", source = "start", qualifiedByName = "convertToTimestamp")
    @Mapping(target = "end", source = "end", qualifiedByName = "convertToTimestamp")*/
    BookingDto toBookingDto(Booking booking);

    //@Named("convertToTimestamp")
    //default Timestamp convertToTimestamp (LocalDateTime localDateTime){return Timestamp.valueOf(LocalDateTime);}
}
