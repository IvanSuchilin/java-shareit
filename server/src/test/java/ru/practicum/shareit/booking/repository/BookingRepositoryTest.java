package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");
    private final Pageable pageable = PageRequest.of(0, 10, sort);
    private User user1;
    private User user2;
    private  Item item;
    @Autowired
    private TestEntityManager em;
    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setEmail("emailtest1@mail.ru");
        user1.setName("name1");
        em.persist(user1);

        user2 = new User();
        user2.setEmail("email2test@mail.ru");
        user2.setName("name2");
        em.persist(user2);

        item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user1);
        em.persist(item);
    }

    @Test
    void findCurrentByBooker() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findCurrentByBooker(user2, LocalDateTime.now(), pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findPastByBooker() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(10));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findPastByBooker(user2, LocalDateTime.now(), pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findFutureByBooker() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(12));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findFutureByBooker(user2, LocalDateTime.now(), pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByOwnerItems() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findAllByOwnerItems(user1, pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findCurrentByOwnerItems() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findCurrentByOwnerItems(user1, LocalDateTime.now(), pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findPastByOwnerItems() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(10));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findPastByOwnerItems(user1, LocalDateTime.now(), pageable);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findFutureByOwnerItems() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(12));
        booking.setStatus(Booking.BookingStatus.APPROVED);
        em.persist(booking);

        List<Booking> bookings = bookingRepository.findFutureByOwnerItems(user1, LocalDateTime.now(), pageable);

        assertEquals(bookings.size(), 1);
    }
}