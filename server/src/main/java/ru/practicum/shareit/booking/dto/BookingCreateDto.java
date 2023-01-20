package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {
    private Long itemId;
    @NotNull(message = "необходимо задать start")
    @Future(message = "начало бронирования не может быть в прошлом")
    private LocalDateTime start;
    @Future(message = "конец бронирования не может быть в прошлом")
    private LocalDateTime end;
    private User booker;
}

