package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository <Booking, Long> {
     List<Booking> findAllByBookerOrderByStartDesc (User booker);
@Query("select b from Booking b where b.booker = :booker and :now between b.start and b.end order by b.start desc")
     List<Booking> findCurrentByBooker(@Param("booker") User userStored, @Param("now") LocalDateTime now);
}
