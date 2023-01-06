package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.request.dto.ItemRequestCreatingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void createTest() {
        ItemRequestCreatingDto itemRequestCreatingDto = new ItemRequestCreatingDto("name");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", new User(), LocalDateTime.now());
        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemRequestCreatingDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"));
    }

    @SneakyThrows
    @Test
    void getUsersRequests() {
        RequestResponseDto requestResponseDto = new RequestResponseDto(1L, "description",
                LocalDateTime.now(), null);
        List<RequestResponseDto> list = new ArrayList<>();
        list.add(requestResponseDto);
        when(userService.getUserById(anyLong())).thenReturn(new UserDto());
        when(itemRequestService.getAllByUserId(anyLong()))
                .thenReturn(list);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is(requestResponseDto.getDescription())));
    }
    @SneakyThrows
    @Test
    void getRequestById() {
        RequestResponseDto requestResponseDto = new RequestResponseDto(1L, "description",
                LocalDateTime.now(), null);
        when(userService.getUserById(anyLong())).thenReturn(new UserDto());
        when(itemRequestService.getRequestById(1L))
                .thenReturn(requestResponseDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"));
    }
    @SneakyThrows
    @Test
    void getAllRequestsWithPagination() {
        RequestResponseDto requestResponseDto = new RequestResponseDto(1L, "description",
                LocalDateTime.now(), null);
        List<RequestResponseDto> list = new ArrayList<>();
        list.add(requestResponseDto);
        when(userService.getUserById(anyLong())).thenReturn(new UserDto());
        when(itemRequestService.getAllRequestsWithPagination(anyInt(), anyInt(), anyLong()))
                .thenReturn(list);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is(requestResponseDto.getDescription())));
    }
}