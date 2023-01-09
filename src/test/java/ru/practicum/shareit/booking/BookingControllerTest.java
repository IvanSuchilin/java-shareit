package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BookingExceptions.InvalidBookingDtoException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.user.dto.UserBookingDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {
    private User booker;
    private UserDto userDto;
    ItemBookingDto itemBookingDto;
    UserBookingDto userBookingDto;
    LocalDateTime startBooking;
    LocalDateTime endBooking;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        booker = new User(1L, "userName", "e@mail");
        userDto = new UserDto(1L, "userName", "e@mail");
        itemBookingDto = new ItemBookingDto(1L, "itemName");
        userBookingDto = new UserBookingDto(1L, "userName");
        startBooking = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
        endBooking = LocalDateTime.now().plusHours(10).truncatedTo(ChronoUnit.SECONDS);
    }

    @SneakyThrows
    @Test
    void createTest() {
        BookingCreateDto bookingCreateDtoCreate = new BookingCreateDto(1L, startBooking, endBooking, booker);
        BookingDto bookingDto =
                new BookingDto(1L, startBooking, endBooking, itemBookingDto, userBookingDto, Booking.BookingStatus.WAITING);
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingService.create(anyLong(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(bookingCreateDtoCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").value(bookingCreateDtoCreate.getEnd().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value(bookingCreateDtoCreate.getStart().toString()));
    }

    @SneakyThrows
    @Test
    void createTestWrongBookingCreateDto() {
        BookingCreateDto bookingCreateDtoWrongCreate = new BookingCreateDto();
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingService.create(anyLong(), any()))
                .thenThrow(new InvalidBookingDtoException("Отсутствуют необходимые данные для создания Booking"));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(bookingCreateDtoWrongCreate)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn().getResponse();
    }

    @SneakyThrows
    @Test
    void getBookingByIdTest() {
        BookingDto bookingDtoGet =
                new BookingDto(1L, startBooking, endBooking, itemBookingDto, userBookingDto, Booking.BookingStatus.WAITING);
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDtoGet);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").value(bookingDtoGet.getEnd().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value(bookingDtoGet.getStart().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value(bookingDtoGet.getBooker().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value(bookingDtoGet.getItem().getName()));
    }

    @SneakyThrows
    @Test
    void updateStatusTest() {
        BookingDto bookingDtoUpd =
                new BookingDto(1L, startBooking, endBooking, itemBookingDto, userBookingDto, Booking.BookingStatus.APPROVED);
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingService.updateApproving(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDtoUpd);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").value(bookingDtoUpd.getEnd().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value(bookingDtoUpd.getStart().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value(bookingDtoUpd.getBooker().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value(bookingDtoUpd.getItem().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(bookingDtoUpd.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void getAllTest() {
        BookingDto bookingDtoAll =
                new BookingDto(1L, startBooking, endBooking, itemBookingDto, userBookingDto, Booking.BookingStatus.REJECTED);
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(bookingDtoAll);
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingService.getAll(anyLong(), any(), any())).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start", is(bookingDtoAll.getStart().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is(bookingDtoAll.getStatus().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end", is(bookingDtoAll.getEnd().toString())));
    }

    @SneakyThrows
    @Test
    void getAllToOwnerTest() {
        BookingDto bookingDtoAllOwner =
                new BookingDto(1L, startBooking, endBooking, itemBookingDto, userBookingDto, Booking.BookingStatus.REJECTED);
        List<BookingDto> bookingsAllOwner = new ArrayList<>();
        bookingsAllOwner.add(bookingDtoAllOwner);
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingService.getAllOwnersBooking(anyLong(), any(), any())).thenReturn(bookingsAllOwner);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start", is(bookingDtoAllOwner.getStart().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is(bookingDtoAllOwner.getStatus().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end", is(bookingDtoAllOwner.getEnd().toString())));
    }
}