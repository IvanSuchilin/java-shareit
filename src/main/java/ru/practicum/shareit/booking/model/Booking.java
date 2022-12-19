package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(schema = "public", name = "bookings")
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status", nullable = false)
    private BookingStatus status;

    public enum BookingStatus {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }

    public enum BookingState {
        ALL,
        CURRENT,
        PAST,
        FUTURE,
        WAITING,
        REJECTED,
        UNSUPPORTED;

        public static BookingState getBookingStateFromQuery(String value) {
            return Arrays.stream(values())
                    .filter(item -> Objects.equals(value, item.name()))
                    .findFirst()
                    .orElse(BookingState.UNSUPPORTED);
        }
    }
}
