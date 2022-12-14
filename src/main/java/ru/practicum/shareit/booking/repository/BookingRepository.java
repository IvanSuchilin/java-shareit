package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerOrderByStartDesc(User booker, Pageable pageable);

    @Query(value = "select b from Booking b where b.booker = :booker and :now between b.start and b.end order by b.start desc")
    List<Booking> findCurrentByBooker(@Param("booker") User userStored, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.booker = :booker and b.end < :now order by b.start desc")
    List<Booking> findPastByBooker(@Param("booker") User userStored, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.booker = :booker and b.start > :now order by b.start desc")
    List<Booking> findFutureByBooker(@Param("booker") User userStored, @Param("now") LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerAndStatusOrderByStartDesc(@Param("booker") User userStored,
                                                           @Param("status") Booking.BookingStatus bookingStatus, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner = :owner order by b.start desc")
    List<Booking> findAllByOwnerItems(@Param("owner") User ownerStored, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner = :owner and :now between b.start and b.end order by b.start desc")
    List<Booking> findCurrentByOwnerItems(@Param("owner") User ownerStored, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner = :owner and b.end < :now order by b.start desc")
    List<Booking> findPastByOwnerItems(@Param("owner") User ownerStored, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.item.owner = :owner and b.start > :now order by b.start desc")
    List<Booking> findFutureByOwnerItems(@Param("owner") User ownerStored, @Param("now") LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItemOwnerAndAndStatusOrderByStart(@Param("owner") User ownerStored,
                                                             @Param("status") Booking.BookingStatus bookingStatus, Pageable pageable);

    Booking findFirstByItemAndStartBeforeOrderByStartDesc(Item item, LocalDateTime start);

    Booking findFirstByItemAndStartAfterOrderByStartDesc(Item item, LocalDateTime start);

    Boolean existsByItemAndAndBookerAndEndBefore(Item item, User booker, LocalDateTime now);
}
